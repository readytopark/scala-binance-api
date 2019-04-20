package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class TickerChange(
  symbol: String,
  priceChange: Double,
  priceChangePercent: Double,
  weightedAvgPrice: Double,
  prevClosePrice: Double,
  lastPrice: Double,
  lastQty: Double,
  bidPrice: Double,
  askPrice: Double,
  openPrice: Double,
  highPrice: Double,
  lowPrice: Double,
  volume: Double,
  quoteVolume: Double,
  openTime: Long,
  closeTime: Long,
  firstId: Long,
  lastId: Long,
  count: Int
)



object TickerChange {
  implicit def decode: Decoder[TickerChange] = deriveDecoder[TickerChange]
}
