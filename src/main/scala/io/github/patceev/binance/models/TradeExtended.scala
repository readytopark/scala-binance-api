package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class TradeExtended(
  symbol: String,
  id: Int,
  orderId: Long,
  price: Double,
  qty: Double,
  quoteQty: Double,
  commission: Double,
  commissionAsset: String,
  time: Long,
  isBuyer: Boolean,
  isMaker: Boolean,
  isBestMatch: Boolean
)

object TradeExtended {
  implicit val decode: Decoder[TradeExtended] = deriveDecoder[TradeExtended]
}
