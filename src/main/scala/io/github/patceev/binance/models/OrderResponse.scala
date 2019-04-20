package io.github.patceev.binance.models

import cats.syntax.functor._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.github.patceev.binance.models.enums._

sealed trait OrderResponse {
  def symbol: String
  def orderId: Long
  def clientOrderId: String
  def transactTime: Long
}

case object OrderResponse {

  case class OrderResponseFull(
    symbol: String,
    orderId: Long,
    clientOrderId: String,
    transactTime: Long,
    price: Double,
    origQty: Double,
    executedQty: Double,
    cummulativeQuoteQty: Double,
    status: OrderStatus,
    timeInForce: TimeInForce,
    `type`: OrderType,
    side: OrderSide,
    fills: List[Fill]
  ) extends OrderResponse

  object OrderResponseFull {
    implicit val decode: Decoder[OrderResponseFull] = deriveDecoder[OrderResponseFull]
  }

  case class OrderResponseResult(
    symbol: String,
    orderId: Long,
    clientOrderId: String,
    transactTime: Long,
    price: Double,
    origQty: Double,
    executedQty: Double,
    cummulativeQuoteQty: Double,
    status: OrderStatus,
    timeInForce: TimeInForce,
    `type`: OrderType,
    side: OrderSide
  ) extends OrderResponse

  object OrderResponseResult {
    implicit val decode: Decoder[OrderResponseResult] = deriveDecoder[OrderResponseResult]
  }

  case class OrderResponseAck(
    symbol: String,
    orderId: Long,
    clientOrderId: String,
    transactTime: Long
  ) extends OrderResponse

  object OrderResponseAck {
    implicit val decode: Decoder[OrderResponseAck] = deriveDecoder[OrderResponseAck]
  }

  implicit val decodeOrderResponse: Decoder[OrderResponse] =
    List[Decoder[OrderResponse]](
      Decoder[OrderResponseFull].widen,
      Decoder[OrderResponseResult].widen,
      Decoder[OrderResponseAck].widen
    ).reduceLeft(_ or _)

}
