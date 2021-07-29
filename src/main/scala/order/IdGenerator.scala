package org.example.testsscala
package order

import java.util.UUID

object IdGenerator {

  def generate(prefix: String = ""): String = {
    val uuid = UUID.randomUUID()
    val bytes = Array[Byte](0) ++ longToBytes(uuid.getMostSignificantBits) ++ longToBytes(uuid.getLeastSignificantBits)

    prefix + BigInt(bytes).toString(36)
  }

  def longToBytes(l: Long): Array[Byte] = {
    val size = 8
    Array.tabulate[Byte](size) { index =>
      (l >>> (size - 1 - index)).toByte
    }
  }

}
