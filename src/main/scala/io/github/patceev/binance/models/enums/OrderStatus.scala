package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait OrderStatus extends EnumEntry

case object OrderStatus extends Enum[OrderStatus] with CirceEnum[OrderStatus] {
  case object NEW extends OrderStatus
  case object PARTIALLY_FILLED extends OrderStatus
  case object FILLED extends OrderStatus
  case object CANCELED extends OrderStatus
  case object PENDING_CANCEL extends OrderStatus
  case object REJECTED extends OrderStatus
  case object EXPIRED extends OrderStatus

  val values = findValues
}
