package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class BookTicker(
  symbol: String,
  bidPrice: Double,
  bidQty: Double,
  askPrice: Double,
  askQty: Double
)

object BookTicker {
  implicit val decode: Decoder[BookTicker] = deriveDecoder[BookTicker]
}
