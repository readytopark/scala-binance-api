package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.github.patceev.binance.models.enums.{OrderSide, OrderStatus, OrderType, TimeInForce}

case class OrderCancellation(
  symbol: String,
  orderId: Long,
  origClientOrderId: String,
  clientOrderId: String,
  price: Double,
  origQty: Double,
  executedQty: Double,
  cummulativeQuoteQty: Double,
  status: OrderStatus,
  timeInForce: TimeInForce,
  `type`: OrderType,
  side: OrderSide
)

object OrderCancellation {
  implicit def decode: Decoder[OrderCancellation] = deriveDecoder[OrderCancellation]
}
