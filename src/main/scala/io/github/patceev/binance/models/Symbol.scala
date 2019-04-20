package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto._
import io.github.patceev.binance.models.enums.SymbolStatus

case class Symbol(
  symbol: String,
  status: SymbolStatus,
  baseAsset: String,
  quoteAsset: String,
  icebergAllowed: Boolean,
  isSpotTradingAllowed: Boolean,
  isMarginTradingAllowed: Boolean
)

object Symbol {
  implicit val decode: Decoder[Symbol] = deriveDecoder[Symbol]
}

