package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait TimeInForce extends EnumEntry

case object TimeInForce extends Enum[TimeInForce] with CirceEnum[TimeInForce] {
  case object GTC extends TimeInForce
  case object IOC extends TimeInForce
  case object FOK extends TimeInForce

  val values = findValues

  implicit def toSome(t: TimeInForce): Option[TimeInForce] = Some(t)
}
