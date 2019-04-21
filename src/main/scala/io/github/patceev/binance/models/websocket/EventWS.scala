package io.github.patceev.binance.models.websocket

import io.github.patceev.binance.models.enums._
import cats.syntax.functor._
import io.circe.{Decoder, HCursor}
import io.github.patceev.binance.models.websocket.StreamWS.{AggregatedTradeStream, CandleStream, TradeStream}

sealed trait EventWS {
  def eventTime: Long
  def stream: StreamWS
}

case object EventWS {

  case class CandleEvent(
    eventTime: Long,
    stream: CandleStream,
    candle: CandleWS
  ) extends EventWS

  case class AggregatedTradeEvent(
    eventTime: Long,
    stream: AggregatedTradeStream = AggregatedTradeStream("test"),
    aggregatedTrade: AggregatedTradeWS
  ) extends EventWS

  case class TradeEvent(
    eventTime: Long,
    stream: TradeStream = TradeStream("test"),
    trade: TradeWS
  ) extends EventWS

  object CandleEvent {
    implicit val decode: Decoder[CandleEvent] = (c: HCursor) => {
      for {
        eventTime <- c.downField("E").as[Long]
        symbol <- c.downField("s").as[String]
        interval <- c.downField("k").downField("i").as[CandleInterval]
        candle <- c.as[CandleWS]
      } yield {
        CandleEvent(eventTime, CandleStream(symbol, interval), candle)
      }
    }
  }

  object AggregatedTradeEvent {
    implicit val decode: Decoder[AggregatedTradeEvent] = (c: HCursor) => {
      for {
        eventTime <- c.downField("E").as[Long]
        symbol <- c.downField("s").as[String]
        aggregatedTrade <- c.as[AggregatedTradeWS]
      } yield {
        AggregatedTradeEvent(eventTime, AggregatedTradeStream(symbol), aggregatedTrade)
      }
    }
  }

  object TradeEvent {
    implicit val decode: Decoder[TradeEvent] = (c: HCursor) => {
      for {
        eventTime <- c.downField("E").as[Long]
        symbol <- c.downField("s").as[String]
        trade <- c.as[TradeWS]
      } yield {
        TradeEvent(eventTime, TradeStream(symbol), trade)
      }
    }
  }

  implicit val decode: Decoder[EventWS] =
    List[Decoder[EventWS]](
      Decoder[CandleEvent].widen,
      Decoder[AggregatedTradeEvent].widen,
      Decoder[TradeEvent].widen
    ).reduceLeft(_ or _)
}

