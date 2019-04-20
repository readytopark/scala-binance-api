package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class Balance(
  asset: String,
  free: Double,
  locked: Double
)

object Balance {
  implicit val decode: Decoder[Balance] = deriveDecoder[Balance]
}
