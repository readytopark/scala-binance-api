package io.github.patceev.binance

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import io.circe.parser._
import io.github.patceev.binance.models.websocket.CloseEvent.{WebsocketErrorCloseEvent, WebsocketGracefulCloseEvent}
import io.github.patceev.binance.models.websocket.{EventWS, StreamWS}

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}
import scala.util.{Failure, Success}

object BinanceWsAPI {

  def initStreams(
    receiver: ActorRef,
    streams: Seq[StreamWS]
  )(implicit s: ActorSystem, m: ActorMaterializer, ec: ExecutionContextExecutor): Future[Done.type] = {

    def handleMessage(message: Message): Unit = message match {
      case m: TextMessage.Strict =>
        decode[EventWS](m.text).toOption match {
          case Some(event) => receiver ! event
          case None => println("Decoding error in stream")
        }
      case _ => ()
    }

    val flow: Flow[Message, Message, (Future[Done], Promise[Option[Message]])] =
      Flow.fromSinkAndSourceMat(
        Sink.foreach[Message](handleMessage),
        Source.maybe[Message]
      )(Keep.both)

    val uri = candleStreamsUri(streams)

    val (upgradeResponse, (sinkClose, _)) =
      Http().singleWebSocketRequest(
        WebSocketRequest(uri),
        flow
      )

    sinkClose.onComplete {
      case Success(_) =>
        receiver ! WebsocketGracefulCloseEvent(System.currentTimeMillis, streams)
      case Failure(exception) =>
        receiver ! WebsocketErrorCloseEvent(System.currentTimeMillis, streams, exception)
    }

    upgradeResponse.map { upgrade =>
      if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
        Done
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }
  }

  private def candleStreamsUri(streams: Seq[StreamWS]) = {
    streams.foldLeft(baseUrl) { (acc, stream) =>
      s"$acc${stream.format}/"
    }.dropRight(1)
  }

  private val baseUrl = "wss://stream.binance.com:9443/ws/"
}
