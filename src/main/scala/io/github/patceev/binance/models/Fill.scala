package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class Fill(
  price: Double,
  qty: Double,
  commission: Double,
  commissionAsset: String
)

object Fill {
  implicit val decode: Decoder[Fill] = deriveDecoder[Fill]
}