package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class Price(
  symbol: String,
  price: Double
)

object Price {
  implicit def decode: Decoder[Price] = deriveDecoder[Price]
}