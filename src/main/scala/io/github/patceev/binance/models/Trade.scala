package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class Trade(
  id: Int,
  price: Double,
  qty: Double,
  time: Long,
  isBuyerMaker: Boolean,
  isBestMatch: Boolean
)

object Trade {
  implicit val decode: Decoder[Trade] = deriveDecoder[Trade]
}
