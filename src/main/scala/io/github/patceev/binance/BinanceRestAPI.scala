package io.github.patceev.binance

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods
import akka.stream.Materializer
import io.github.patceev.binance.models.OrderResponse.OrderResponseFull
import io.github.patceev.binance.models._
import io.github.patceev.binance.models.enums._
import io.github.patceev.binance.requests.BinanceRequestHelper
import io.github.patceev.binance.requests.BinanceRequestHelper.{optionalParam, optionalParams, stdParams}
import io.github.patceev.binance.util.Helpers._

import scala.concurrent.{ExecutionContext, Future}

object BinanceRestAPI {

  object GeneralEndpoints {

    def time(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[ExchangeTime] = {
      BinanceRequestHelper.unsignedRequest[ExchangeTime](
        uri = "/exchangeInfo",
        method = HttpMethods.GET,
        params = Map.empty,
        version = "v1"
      )
    }

    def exchangeInfo(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[ExchangeInfo] = {
      BinanceRequestHelper.unsignedRequest[ExchangeInfo](
        uri = "/exchangeInfo",
        method = HttpMethods.GET,
        params = Map.empty,
        version = "v1"
      )
    }

    def depth(symbol: String, limit: Option[Int] = None)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Depth] =
      BinanceRequestHelper.unsignedRequest[Depth](
        uri = "/depth",
        method = HttpMethods.GET,
        params = Map("symbol" -> symbol) ++ optionalParam("limit", limit),
        version = "v1"
      )

    def trades(symbol: String, limit: Option[Int] = None)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Vector[Trade]] =
      BinanceRequestHelper.unsignedRequest[Vector[Trade]](
        uri = "/trades",
        method = HttpMethods.GET,
        params = Map("symbol" -> symbol) ++ optionalParam("limit", limit),
        version = "v1"
      )

    /*
      This request doesn't work. For some reason, Binance asks for Api Key without documenting it
      in its API documentation. Given key it still returns error because it says that there were
      too much arguments
     */
    def historicalTrades(
      symbol: String,
      limit: Option[Int] = None,
      fromId: Option[Long] = None
    )(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[Vector[Trade]] =
      BinanceRequestHelper.unsignedRequest[Vector[Trade]](
        uri = "/historicalTrades",
        method = HttpMethods.GET,
        params = Map("symbol" -> symbol) ++ optionalParams("limit" -> limit, "fromId" -> fromId),
        version = "v1"
      )

    def aggTrades(
      symbol: String,
      fromId: Option[Long] = None,
      startTime: Option[Long] = None,
      endTime: Option[Long] = None,
      limit: Option[Int] = None
    )(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[Vector[AggregatedTrade]] = {
      val optParams = optionalParams(
        "fromId" -> fromId,
        "startTime" -> startTime,
        "endTime" -> endTime,
        "limit" -> limit
      )

      BinanceRequestHelper.unsignedRequest[Vector[AggregatedTrade]](
        uri = "/aggTrades",
        method = HttpMethods.GET,
        params = Map("symbol" -> symbol) ++ optParams,
        version = "v1"
      )
    }

    def klines(
      symbol: String,
      interval: CandleInterval,
      startTime: Option[Long] = None,
      endTime: Option[Long] = None,
      limit: Option[Int] = None
    )(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[Vector[Candle]] = {
      val requiredParams = Map("symbol" -> symbol, "interval" -> interval.toString)

      val optParams = optionalParams(
        "startTime" -> startTime,
        "endTime" -> endTime,
        "limit" -> limit
      )

      BinanceRequestHelper.unsignedRequest[Vector[Candle]](
        uri = "/klines",
        method = HttpMethods.GET,
        params = requiredParams ++ optParams,
        version = "v1"
      )
    }

    def avgPrice(symbol: String)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[AveragePrice] =
      BinanceRequestHelper.unsignedRequest[AveragePrice](
        uri = "/avgPrice",
        method = HttpMethods.GET,
        params = Map("symbol" -> symbol),
        version = "v3"
      )

    def change24h(symbol: Option[String] = None)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Vector[TickerChange]] = {
      val tickerChanges = BinanceRequestHelper.unsignedRequest[Vector[TickerChange]](
        uri = "/ticker/24hr",
        method = HttpMethods.GET,
        params = Map.empty,
        version = "v1"
      )

      symbol match {
        case Some(s) => tickerChanges.map(_.filter(_.symbol == s))
        case None => tickerChanges
      }
    }

    def price(symbol: Option[String] = None)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Vector[Price]] = {
      val prices = BinanceRequestHelper.unsignedRequest[Vector[Price]](
        uri = "/ticker/price",
        method = HttpMethods.GET,
        params = Map.empty,
        version = "v3"
      )

      symbol match {
        case Some(s) => prices.map(_.filter(_.symbol == s))
        case None => prices
      }
    }

    def bookTicker(symbol: Option[String] = None)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Vector[BookTicker]] = {
      val bookTickers = BinanceRequestHelper.unsignedRequest[Vector[BookTicker]](
        uri = "/ticker/bookTicker",
        method = HttpMethods.GET,
        params = Map.empty,
        version = "v3"
      )

      symbol match {
        case Some(s) => bookTickers.map(_.filter(_.symbol == s))
        case None => bookTickers
      }
    }
  }

  object AccountEndpoints {

    def newTestOrder(
      symbol: String,
      side: OrderSide,
      `type`: OrderType,
      quantity: Double,
      timestamp: Long = System.currentTimeMillis,
      recvWindow: Option[Long] = None
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Unit] = {
      BinanceRequestHelper.signedRequest[Unit](
        uri = "/order/test",
        method = HttpMethods.POST,
        params = Map(
          "symbol" -> symbol,
          "side" -> side.toString,
          "type" -> `type`.toString,
          "quantity" -> quantity.toString
        ) ++ stdParams(timestamp, recvWindow),
        version = "v3"
      )
    }

    def newOrder(
      symbol: String,
      side: OrderSide,
      `type`: OrderType,
      timeInForce: Option[TimeInForce] = None,
      quantity: Double,
      price: Option[Double] = None,
      newClientOrderId: Option[String] = None,
      stopPrice: Option[Double] = None,
      icebergQty: Option[Double] = None,
      newOrderRespType: Option[OrderResponseType] = None,
      recvWindow: Option[Long] = None,
      timestamp: Long = System.currentTimeMillis
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[OrderResponseFull] = {
      val requiredParams = Map(
        "symbol" -> symbol,
        "side" -> side.toString,
        "type" -> `type`.toString,
        "quantity" -> quantity.toString
      )

      println(price.map(_.toPriceFormat))

      val optParams = optionalParams(
        "timeInForce" -> timeInForce,
        "price" -> price.map(_.toPriceFormat),
        "newClientOrderId" -> newClientOrderId,
        "stopPrice" -> stopPrice.map(_.toPriceFormat),
        "icebergQty" -> icebergQty,
        "newOrderRespType" -> Some(OrderResponseType.withName("FULL"))
      )

      val standardParams = stdParams(timestamp, recvWindow)

      BinanceRequestHelper.signedRequest[OrderResponseFull](
        "/order",
        HttpMethods.POST,
        requiredParams ++ optParams ++ standardParams,
        "v3"
      )
    }

    def orderStatus(
      symbol: String,
      orderId: Option[Long] = None,
      origClientOrderId: Option[String] = None,
      recvWindow: Option[Long]  = None,
      timestamp: Long = System.currentTimeMillis
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Order] = {
      val optParams = optionalParams(
        "orderId" -> orderId,
        "origClientOrderId" -> origClientOrderId,
      )

      BinanceRequestHelper.signedRequest[Order](
        uri = "/order",
        method = HttpMethods.GET,
        params = Map("symbol" -> symbol) ++ optParams ++ stdParams(timestamp, recvWindow),
        version = "v3"
      )
    }

    def cancelOrder(
      symbol: String,
      orderId: Option[Long],
      origClientOrderId: Option[String] = None,
      newClientOrderId: Option[String] = None,
      recvWindow: Option[Long] = None,
      timestamp: Long = System.currentTimeMillis
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[OrderCancellation] = {
      val optParams = optionalParams(
        "orderId" -> orderId,
        "origClientOrderId" -> origClientOrderId,
        "newClientOrderId" -> newClientOrderId,
      )

      BinanceRequestHelper.signedRequest[OrderCancellation](
        uri = "/order",
        method = HttpMethods.DELETE,
        params = Map("symbol" -> symbol) ++ optParams ++ stdParams(timestamp, recvWindow),
        version = "v3"
      )
    }

    def openOrders(
      symbol: Option[String] = None,
      recvWindow: Option[Long] = None,
      timestamp: Long = System.currentTimeMillis,
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Vector[Order]] = {
      val optParams = optionalParams("symbol" -> symbol)

      BinanceRequestHelper.signedRequest[Vector[Order]](
        uri = "/openOrders",
        method = HttpMethods.GET,
        params = optParams ++ stdParams(timestamp, recvWindow),
        version = "v3"
      )
    }

    def allOrders(
      symbol: String,
      orderId: Option[Long] = None,
      startTime: Option[Long] = None,
      endTime: Option[Long] = None,
      limit: Option[Int] = None,
      recvWindow: Option[Long] = None,
      timestamp: Long = System.currentTimeMillis
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Vector[Order]] = {
      val requiredParams = Map("symbol" -> symbol)

      val optParams = optionalParams(
        "orderId" -> orderId,
        "startTime" -> startTime,
        "endTime" -> endTime,
        "limit" -> limit,
      )

      BinanceRequestHelper.signedRequest[Vector[Order]](
        uri = "/allOrders",
        method = HttpMethods.GET,
        params = requiredParams ++ optParams ++ stdParams(timestamp, recvWindow),
        version = "v3"
      )
    }

    def account(
      timestamp: Long = System.currentTimeMillis,
      recvWindow: Option[Long] = None
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[AccountInformation] = {
      BinanceRequestHelper.signedRequest[AccountInformation](
        uri = "/account",
        method = HttpMethods.GET,
        params = stdParams(timestamp, recvWindow),
        version = "v3"
      )
    }

    def myTrades(
      symbol: String,
      startTime: Option[Long] = None,
      endTime: Option[Long] = None,
      fromId: Option[Long] = None,
      limit: Option[Int] = None,
      recvWindow: Option[Long] = None,
      timestamp: Long = System.currentTimeMillis
    )(
      implicit binanceConfiguration: BinanceConfiguration, ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Vector[TradeExtended]] = {
      val optParams = optionalParams(
        "startTime" -> startTime,
        "endTime" -> endTime,
        "fromId" -> fromId,
        "limit" -> limit
      )

      BinanceRequestHelper.signedRequest[Vector[TradeExtended]](
        uri = "/myTrades",
        method = HttpMethods.GET,
        params = Map("symbol" -> symbol) ++ optParams ++ stdParams(timestamp, recvWindow),
        version = "v3"
      )
    }

    /*
      User streams don't work yet
     */

    def startUserDataStream(implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem): Future[ListenKey] =
      BinanceRequestHelper.unsignedRequest[ListenKey](
        uri = "/userDataStream",
        method = HttpMethods.POST,
        params = Map.empty,
        version = "v1"
      )

    def keepAliveUserDataStream(listenKey: String)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Unit] =
      BinanceRequestHelper.unsignedRequest[Unit](
        uri = "/userDataStream",
        method = HttpMethods.PUT,
        params = Map("listenKey" -> listenKey),
        version = "v1"
      )

    def deleteUserDataStream(listenKey: String)(
      implicit ec: ExecutionContext, mat: Materializer, as: ActorSystem
    ): Future[Unit] =
      BinanceRequestHelper.unsignedRequest[Unit](
        uri = "/userDataStream",
        method = HttpMethods.DELETE,
        params = Map("listenKey" -> listenKey),
        version = "v1"
      )
  }
}
