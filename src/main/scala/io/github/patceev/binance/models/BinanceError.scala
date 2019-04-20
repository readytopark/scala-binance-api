package io.github.patceev.binance.models

import io.github.patceev.binance.util.Errors.ApiError

trait BinanceError extends Exception{
  val message: String

  override def toString: String = {
    s"[${this.getClass.getName}]: $message"
  }
}

object BinanceError {

  // General Server or Network issues
  case class Unknown(message: String) extends BinanceError
  case class Disconnected(message: String) extends BinanceError
  case class Unauthorized(message: String) extends BinanceError
  case class TooManyRequests(message: String) extends BinanceError
  case class UnexpectedResp(message: String) extends BinanceError
  case class Timeout(message: String) extends BinanceError
  case class UnknownOrderComposition(message: String) extends BinanceError
  case class TooManyOrders(message: String) extends BinanceError
  case class ServiceShuttingDown(message: String) extends BinanceError
  case class UnsupportedOperation(message: String) extends BinanceError
  case class InvalidTimestamp(message: String) extends BinanceError
  case class InvalidSignature(message: String) extends BinanceError

  // Matching engine
  case class ErrorMsgReceived(message: String) extends BinanceError

  // Request issues
  case class IllegalChars(message: String) extends BinanceError
  case class TooManyParameters(message: String) extends BinanceError
  case class MandatoryParamEmptyOrMalformed(message: String) extends BinanceError
  case class UnknownParam(message: String) extends BinanceError
  case class UnreadParameters(message: String) extends BinanceError
  case class ParamEmpty(message: String) extends BinanceError
  case class ParamNotRequired(message: String) extends BinanceError
  case class BadPrecision(message: String) extends BinanceError
  case class NoDeppth(message: String) extends BinanceError
  case class TifNotRequired(message: String) extends BinanceError
  case class InvalidTif(message: String) extends BinanceError
  case class InvalidOrderType(message: String) extends BinanceError
  case class InvalidSide(message: String) extends BinanceError
  case class EmptyNewClOrdId(message: String) extends BinanceError
  case class EmptyOrgClOrdId(message: String) extends BinanceError
  case class BadInterval(message: String) extends BinanceError
  case class BadSymbol(message: String) extends BinanceError
  case class InvalidListenKey(message: String) extends BinanceError
  case class MoreThanXXHours(message: String) extends BinanceError
  case class OptionalParamsBadCombo(message: String) extends BinanceError
  case class InvalidParameter(message: String) extends BinanceError
  case class NewOrderRejected(message: String) extends BinanceError
  case class CancelRejected(message: String) extends BinanceError
  case class NoSuchOrder(message: String) extends BinanceError
  case class BadApiKeyFmt(message: String) extends BinanceError
  case class RejectedMbxKey(message: String) extends BinanceError
  case class NoTradingWindow(message: String) extends BinanceError

  // Filter failures
  case class FilterFailure(message: String) extends BinanceError

  case class NotDocumentedFailure(message: String) extends BinanceError

  def fromApiError(apiError: ApiError): BinanceError = {
    val error = apiError.code match {
      case -1000 => Unknown
      case -1001 => Disconnected
      case -1002 => Unauthorized
      case -1003 => TooManyRequests
      case -1006 => UnexpectedResp
      case -1007 => Timeout
      case -1014 => UnknownOrderComposition
      case -1015 => TooManyOrders
      case -1016 => ServiceShuttingDown
      case -1020 => UnsupportedOperation
      case -1021 => InvalidTimestamp
      case -1022 => InvalidSignature
      case -1100 => IllegalChars
      case -1101 => TooManyParameters
      case -1102 => MandatoryParamEmptyOrMalformed
      case -1103 => UnknownParam
      case -1104 => UnreadParameters
      case -1105 => ParamEmpty
      case -1106 => ParamNotRequired
      case -1111 => BadPrecision
      case -1112 => NoDeppth
      case -1114 => TifNotRequired
      case -1115 => InvalidTif
      case -1116 => InvalidOrderType
      case -1117 => InvalidSide
      case -1118 => EmptyNewClOrdId
      case -1119 => EmptyOrgClOrdId
      case -1120 => BadInterval
      case -1121 => BadSymbol
      case -1125 => InvalidListenKey
      case -1127 => MoreThanXXHours
      case -1128 => OptionalParamsBadCombo
      case -1130 => InvalidParameter
      case -2010 => NewOrderRejected
      case -2011 => CancelRejected
      case -2013 => NoSuchOrder
      case -2014 => BadApiKeyFmt
      case -2015 => RejectedMbxKey
      case -2016 => NoTradingWindow
      case code if code.toString.startsWith("9") => FilterFailure
      case -1010 => ErrorMsgReceived
      case _ => NotDocumentedFailure

    }
    error(apiError.msg)
  }
}