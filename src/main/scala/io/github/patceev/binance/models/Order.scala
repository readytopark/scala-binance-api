package io.github.patceev.binance.models

import io.circe._
import io.circe.generic.semiauto.deriveDecoder
import io.github.patceev.binance.models.enums._

case class Order(
  symbol: String,
  orderId: Long,
  clientOrderId: String,
  price: Double,
  origQty: Double,
  executedQty: Double,
  cummulativeQuoteQty: Double,
  status: OrderStatus,
  timeInForce: TimeInForce,
  `type`: OrderType,
  side: OrderSide,
  stopPrice: Double,
  icebergQty: Double,
  time: Long,
  updateTime: Long,
  isWorking: Boolean
)

object Order {
  implicit val decode: Decoder[Order] = deriveDecoder[Order]
}