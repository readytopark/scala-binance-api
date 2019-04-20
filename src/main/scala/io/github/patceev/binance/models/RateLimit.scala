package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.github.patceev.binance.models.enums.{RateLimitInterval, RateLimitType}

case class RateLimit(
  rateLimitType: RateLimitType,
  interval: RateLimitInterval,
  intervalNum: Int,
  limit: Int
)

object RateLimit {
  implicit val decode: Decoder[RateLimit] = deriveDecoder[RateLimit]
}
