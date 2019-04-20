package io.github.patceev.binance.models.enums

import enumeratum._
import io.github.patceev.binance.models.OrderResponse._

sealed trait OrderResponseType extends EnumEntry

case object OrderResponseType extends Enum[OrderResponseType] with CirceEnum[OrderResponseType] {
  case object ACK extends OrderResponseType
  case object RESULT extends OrderResponseType
  case object FULL extends OrderResponseType

  val values = findValues

  def toOrderResponse(o: OrderResponseType) = o match {
    case ACK => OrderResponseAck
    case RESULT => OrderResponseResult
    case FULL => OrderResponseFull
  }

  // for the purpose of passing OrderResponseType to functions which expect it to be optional
  implicit def toSome(o: OrderResponseType): Option[OrderResponseType] = Some(o)
}
