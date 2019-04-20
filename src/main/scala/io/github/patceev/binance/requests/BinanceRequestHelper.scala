package io.github.patceev.binance.requests

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpMethod, HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import io.circe.Decoder
import io.circe.parser._
import io.github.patceev.binance.BinanceConfiguration
import io.github.patceev.binance.models.BinanceError
import io.github.patceev.binance.util.Crypto
import io.github.patceev.binance.util.Errors.{ApiError, DecodingError}
import io.github.patceev.binance.util.Helpers._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Left, Right}

object BinanceRequestHelper {

  def signedRequest[Result: Decoder](
    uri: String, method: HttpMethod, params: Map[String, String], version: String
  )(implicit binanceConfig: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[Result] = {
    val queryString = makeQueryString(params)

    val signature = Crypto.generateHMAC(
      data = queryString.getBytes,
      secretKey = binanceConfig.privateKey.getBytes,
      hashFn = "HmacSHA256").toHexString

    val request = HttpRequest(
      uri = s"${baseUri(version)}$uri?$queryString&signature=$signature",
      method = method
    ).withHeaders(RawHeader("X-MBX-APIKEY", binanceConfig.publicKey))

    Http().singleRequest(request).flatMap(handleResponse[Result])
  }

  def unsignedRequest[Result: Decoder](
    uri: String, method: HttpMethod, params: Map[String, String], version: String
  )(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[Result] = {
    val request = HttpRequest(
      uri = s"${baseUri(version)}$uri?${makeQueryString(params)}",
      method = method
    )

    Http().singleRequest(request).flatMap(handleResponse[Result])
  }

  private def handleResponse[Result : Decoder](
    response: HttpResponse
  )(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[Result] = response match {
    case HttpResponse(StatusCodes.OK, _, entity, _) =>
      Unmarshal(entity).to[String].map(decode[Result]).flatMap {
        case Left(e) => Future.failed(DecodingError(e.toString))
        case Right(result) => Future.successful(result)
      }
    case HttpResponse(statusCode, _, entity, _) =>
      Unmarshal(entity).to[String].map(decode[ApiError]).flatMap {
        case Left(e) => Future.failed(DecodingError(e.toString))
        case Right(result) => Future.failed(BinanceError.fromApiError(result))
      }
  }

  def optionalParams(params: (String, Option[Any])*): Map[String, String] = {
    params.foldLeft(Map.empty[String, String]) { case (accum, (k, vOpt)) =>
      accum ++ optionalParam(k, vOpt)
    }
  }

  def optionalParam(name: String, optParam: Option[Any]): Map[String, String] = {
    optParam.map(v => Map(name -> v.toString)).getOrElse(Map.empty)
  }

  def stdParams(timestamp: Long, recvWindow: Option[Long]): Map[String, String] = {
    Map("timestamp" -> timestamp.toString) ++ optionalParam("recvWindow", recvWindow)
  }

  private def makeQueryString(params: Map[String, String]): String = {
    params.foldLeft("") { case (accum, (param, value)) =>
      val prefix = if (accum.isEmpty) "" else s"$accum&"
      s"$prefix${param.encodeForUrl}=${value.encodeForUrl}"
    }
  }

  private def baseUri(version: String) = s"https://api.binance.com/api/$version"
}
