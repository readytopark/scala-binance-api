package io.github.patceev.binance.models

import io.circe.generic.semiauto._
import io.circe.{Decoder, HCursor}
import io.github.patceev.binance.models.Depth.{Ask, Bid}

case class Depth(
  lastUpdateId: Long,
  bids: List[Bid],
  asks: List[Ask]
)

object Depth {
  implicit val decode: Decoder[Depth] = deriveDecoder[Depth]

  case class Bid(price: Double, quantity: Double)

  object Bid {
    implicit val decode: Decoder[Bid] = (c: HCursor) =>
      c.as[(Double, Double)].map(bid => Bid(bid._1, bid._2))
  }

  case class Ask(price: Double, quantity: Double)

  object Ask {
    implicit val decode: Decoder[Ask] = (c: HCursor) =>
      c.as[(Double, Double)].map(ask => Ask(ask._1, ask._2))
  }
}
