package io.github.patceev.binance.models.websocket

import io.circe.{Decoder, HCursor}

case class TradeWS(
  tradeId: Int,
  price: Double,
  quantity: Double,
  buyerOrderId: Long,
  sellerOrderId: Long,
  tradeTime: Long,
  isMarketMaker: Boolean
)

object TradeWS {
  implicit def decode: Decoder[TradeWS] = (c: HCursor) =>
    for {
      tradeId <- c.downField("t").as[Int]
      price <- c.downField("p").as[Double]
      quantity <- c.downField("q").as[Double]
      buyerOrderId <- c.downField("b").as[Long]
      sellerOrderId <- c.downField("a").as[Long]
      tradeTime <- c.downField("T").as[Long]
      isMarketMaker <- c.downField("m").as[Boolean]
    } yield {
      TradeWS(
        tradeId = tradeId,
        price = price,
        quantity = quantity,
        buyerOrderId = buyerOrderId,
        sellerOrderId = sellerOrderId,
        tradeTime = tradeTime,
        isMarketMaker = isMarketMaker
      )
    }
}
