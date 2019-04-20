package io.github.patceev.binance.models

import io.circe.{Decoder, HCursor}

case class Candle(
  openTime: Long,
  open: Double,
  high: Double,
  low: Double,
  close: Double,
  volume: Double,
  closeTime: Long,
)

object Candle {
  implicit val decode: Decoder[Candle] = (c: HCursor) =>
    c.as[(Long, Double, Double, Double, Double, Double, Long, Double, Int, Double, Double, Double)]
    .map(candle =>
      Candle(
        openTime = candle._1,
        open = candle._2,
        high = candle._3,
        low = candle._4,
        close = candle._5,
        volume = candle._6,
        closeTime = candle._7
      )
    )
}
