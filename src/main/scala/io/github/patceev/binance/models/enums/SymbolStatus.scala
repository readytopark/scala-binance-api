package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait SymbolStatus extends EnumEntry

case object SymbolStatus extends Enum[SymbolStatus] with CirceEnum[SymbolStatus] {
  case object PRE_TRADING extends SymbolStatus
  case object TRADING extends SymbolStatus
  case object POST_TRADING extends SymbolStatus
  case object END_OF_DAY extends SymbolStatus
  case object HALT extends SymbolStatus
  case object AUCTION_BREAK extends SymbolStatus
  case object BREAK extends SymbolStatus

  val values = findValues
}
