package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait OrderSide extends EnumEntry

case object OrderSide extends Enum[OrderSide] with CirceEnum[OrderSide] {
  case object BUY extends OrderSide
  case object SELL extends OrderSide

  val values = findValues
}
