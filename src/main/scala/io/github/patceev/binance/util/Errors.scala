package io.github.patceev.binance.util

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

object Errors {
  case class ApiError(code: Int, msg: String) extends Exception {
    override def toString: String = {
      s"[Api Error] code: $code, message: $msg"
    }
  }

  case object TooManyRequestsError extends Exception {
    override def toString: String = {
      "[Too Many Request Error]"
    }
  }

  case class DecodingError(message: String) extends Exception {
    override def toString: String = {
      s"[Decoding Error] message: $message"
    }
  }

  case class IndicatorError(name: String) extends Exception {
    override def toString: String = {
      s"[Indicator Error] name: $name"
    }
  }

  object ApiError {
    implicit val decode: Decoder[ApiError] = deriveDecoder[ApiError]
    implicit val encode: Encoder[ApiError] = deriveEncoder[ApiError]
  }
}
