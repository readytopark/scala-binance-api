package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AveragePrice(mins: Int, price: Double)

object AveragePrice {
  implicit val decode: Decoder[AveragePrice] = deriveDecoder[AveragePrice]
}
