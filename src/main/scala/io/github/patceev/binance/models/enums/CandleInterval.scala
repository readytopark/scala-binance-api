package io.github.patceev.binance.models.enums

import enumeratum._

sealed trait CandleInterval extends EnumEntry

case object CandleInterval extends Enum[CandleInterval] with CirceEnum[CandleInterval] {
  case object `1m` extends CandleInterval
  case object `3m` extends CandleInterval
  case object `5m` extends CandleInterval
  case object `15m` extends CandleInterval
  case object `30m` extends CandleInterval
  case object `1h` extends CandleInterval
  case object `2h` extends CandleInterval
  case object `4h` extends CandleInterval
  case object `6h` extends CandleInterval
  case object `8h` extends CandleInterval
  case object `12h` extends CandleInterval
  case object `1d` extends CandleInterval
  case object `3d` extends CandleInterval
  case object `1w` extends CandleInterval

  lazy val values = findValues

  lazy val valuesCrucial: Seq[CandleInterval] = Seq(
    CandleInterval.withName("30m"),
    CandleInterval.withName("1h"),
    CandleInterval.withName("4h")
  )
}
