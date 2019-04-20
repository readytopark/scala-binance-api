package io.github.patceev.binance.util

import java.net.URLEncoder
import java.text.{DecimalFormatSymbols, SimpleDateFormat}
import java.util.{Date, Locale, TimeZone}

object Helpers {

  object DateFormats {
    val yyyyMMddHHmm = "yyyy.MM.dd HH:mm"
    val yyyyMMddHHmmSS = "yyyy.MM.dd HH:mm:ss"
  }

  def formatTimestamp(stamp: Long, format: String): String = {
    val formatter = new SimpleDateFormat(format)
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
    formatter.format(new Date(stamp))
  }

  implicit class StringExtended(value: String) {
    def encodeForUrl: String = URLEncoder.encode(value, "UTF-8")
  }

  implicit class DoubleExtended(value: Double) {
    def printable: String = f"$value%1.8f"

    def toPriceFormat: String = priceFormatter.format(value)
  }

  implicit class ByteArrayExtended(arr: Array[Byte]) {
    def toHexString: String = arr.map("%02x".format(_)).mkString
  }

  implicit class BooleanExtended(b: Boolean) {
    def toOption[T](x: => T): Option[T] = if (b) Some(x) else None
  }

  val ukLocale = new Locale("en", "UK")
  val symbols = new DecimalFormatSymbols(ukLocale)
  val priceFormatter = new java.text.DecimalFormat("####0.##########", symbols)
}

