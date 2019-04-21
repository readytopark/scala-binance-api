package io.github.patceev.binance.models.websocket

import io.circe.{Decoder, HCursor}

case class CandleWS(
  openTime: Long,
  open: Double,
  high: Double,
  low: Double,
  close: Double,
  volume: Double,
  closeTime: Long,
  isFinal: Boolean
)

object CandleWS {
  implicit val decodeWS: Decoder[CandleWS] = (c: HCursor) => {
    val kline = c.downField("k")
    for {
      openTime <- kline.downField("t").as[Long]
      closeTime <- kline.downField("T").as[Long]
      open <- kline.downField("o").as[Double]
      close <- kline.downField("c").as[Double]
      high <- kline.downField("h").as[Double]
      low <- kline.downField("l").as[Double]
      volume <- kline.downField("V").as[Double]
      isFinal <- kline.downField("x").as[Boolean]
    } yield {
      CandleWS(
        openTime = openTime,
        closeTime = closeTime,
        open = open,
        close = close,
        high = high,
        low = low,
        volume = volume,
        isFinal = isFinal
      )
    }
  }
}