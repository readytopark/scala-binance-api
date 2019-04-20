package io.github.patceev.binance.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AccountInformation(
  makerCommission: Int,
  takerCommission: Int,
  buyerCommission: Int,
  sellerCommission: Int,
  canTrade: Boolean,
  canWithdraw: Boolean,
  canDeposit: Boolean,
  updateTime: Long,
  balances: List[Balance]
)

object AccountInformation {
  implicit val decoder: Decoder[AccountInformation] = deriveDecoder[AccountInformation]
}
