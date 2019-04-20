package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class ListenKey(listenKey: String)

object ListenKey {
  implicit val decode: Decoder[ListenKey] = deriveDecoder[ListenKey]
}
