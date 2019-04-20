package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait RateLimitType extends EnumEntry

case object RateLimitType extends Enum[RateLimitType] with CirceEnum[RateLimitType] {
  case object REQUEST_WEIGHT extends RateLimitType
  case object ORDERS extends RateLimitType
  case object RAW_REQUESTS extends RateLimitType

  val values = findValues
}
