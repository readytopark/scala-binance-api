package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait RateLimitInterval extends EnumEntry

case object RateLimitInterval extends Enum[RateLimitInterval] with CirceEnum[RateLimitInterval] {
  case object SECOND extends RateLimitInterval
  case object MINUTE extends RateLimitInterval
  case object DAY extends RateLimitInterval

  val values = findValues
}
