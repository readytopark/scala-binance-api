package io.github.patceev.binance.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Crypto {

  def generateHMAC(data: Array[Byte], secretKey: Array[Byte], hashFn: String): Array[Byte] = {
    val mac = Mac.getInstance(hashFn)
    mac.init(new SecretKeySpec(secretKey, hashFn))
    mac.doFinal(data)
  }

}
