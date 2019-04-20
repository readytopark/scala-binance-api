package io.github.patceev.binance.models

import io.circe.{Decoder, HCursor}

case class AggregatedTrade(
  id: Int,
  price: Double,
  quantity: Double,
  firstTradeId: Int,
  lastTradeId: Int,
  timestamp: Long,
  isBuyerMaker: Boolean,
  isBestMatch: Boolean
)

object AggregatedTrade {
  implicit def decode: Decoder[AggregatedTrade] = (c: HCursor) =>
    for {
      id <- c.downField("a").as[Int]
      price <- c.downField("p").as[Double]
      quantity <- c.downField("q").as[Double]
      firstTradeId <- c.downField("f").as[Int]
      lastTradeId <- c.downField("l").as[Int]
      timestamp <- c.downField("T").as[Long]
      isBuyerMaker <- c.downField("m").as[Boolean]
      isBestMatch <- c.downField("M").as[Boolean]
    } yield {
      AggregatedTrade(
        id = id,
        price = price,
        quantity = quantity,
        firstTradeId = firstTradeId,
        lastTradeId = lastTradeId,
        timestamp = timestamp,
        isBuyerMaker = isBuyerMaker,
        isBestMatch = isBestMatch
      )
    }
}
