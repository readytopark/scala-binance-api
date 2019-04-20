package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait OrderType extends EnumEntry

case object OrderType extends Enum[OrderType] with CirceEnum[OrderType] {
  case object LIMIT extends OrderType
  case object MARKET extends OrderType
  case object STOP_LOSS extends OrderType
  case object STOP_LOSS_LIMIT extends OrderType
  case object TAKE_PROFIT extends OrderType
  case object TAKE_PROFIT_LIMIT extends OrderType
  case object LIMIT_MAKER extends OrderType

  val values = findValues
}
