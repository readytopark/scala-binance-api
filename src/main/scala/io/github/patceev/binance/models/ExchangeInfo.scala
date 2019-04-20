package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class ExchangeInfo(
  serverTime: Long,
  rateLimits: List[RateLimit],
  symbols: List[Symbol]
)

object ExchangeInfo {
  implicit val decode: Decoder[ExchangeInfo] = deriveDecoder[ExchangeInfo]
}