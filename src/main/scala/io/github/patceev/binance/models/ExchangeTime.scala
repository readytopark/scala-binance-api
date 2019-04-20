package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class ExchangeTime(serverTime: Long)

object ExchangeTime {
  implicit val decode: Decoder[ExchangeTime] = deriveDecoder[ExchangeTime]
}
