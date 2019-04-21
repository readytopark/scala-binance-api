package io.github.patceev.binance.models.websocket

import io.circe.{Decoder, HCursor}

case class AggregatedTradeWS(
  aggregatedTradeId: Int,
  price: Double,
  quantity: Double,
  firstTradeId: Int,
  lastTradeId: Int,
  tradeTime: Long,
  isMarketMaker: Boolean
)

object AggregatedTradeWS {
  implicit def decode: Decoder[AggregatedTradeWS] = (c: HCursor) =>
    for {
      aggregatedTradeId <- c.downField("a").as[Int]
      price <- c.downField("p").as[Double]
      quantity <- c.downField("q").as[Double]
      firstTradeId <- c.downField("f").as[Int]
      lastTradeId <- c.downField("l").as[Int]
      tradeTime <- c.downField("T").as[Long]
      isMarketMaker <- c.downField("m").as[Boolean]
    } yield {
      AggregatedTradeWS(
        aggregatedTradeId = aggregatedTradeId,
        price = price,
        quantity = quantity,
        firstTradeId = firstTradeId,
        lastTradeId = lastTradeId,
        tradeTime = tradeTime,
        isMarketMaker = isMarketMaker
      )
    }
}