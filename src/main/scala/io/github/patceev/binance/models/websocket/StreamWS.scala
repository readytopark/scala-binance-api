package io.github.patceev.binance.models.websocket

import io.github.patceev.binance.models.enums.CandleInterval

trait StreamWS {
  def symbol: String
  def name: String
  def format: String
}

case object StreamWS {
  case class AggregatedTradeStream(symbol: String) extends StreamWS {
    val name = "aggTrade"

    def format = s"${symbol.toLowerCase}@$name"
  }

  case class TradeStream(symbol: String) extends StreamWS {
    val name = "trade"

    def format = s"${symbol.toLowerCase}@$name"
  }

  case class CandleStream(symbol: String, interval: CandleInterval) extends StreamWS {
    def name = "kline_"

    def format = s"${symbol.toLowerCase}@$name$interval"
  }

  implicit def toSeq(s: StreamWS): Seq[StreamWS] = Seq(s)
}

