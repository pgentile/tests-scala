package org.example.testsscala
package exceptions

import org.scalatest.flatspec.AnyFlatSpec

class TryCatchSpec extends AnyFlatSpec{

  it should "rethrow unmatched exceptions" in {
    assertThrows[IndexOutOfBoundsException] {
      try {
        println("Some test")
        throw new IndexOutOfBoundsException
      } catch {
        case e: IllegalStateException => println(s"Caught exception $e")
      }
    }
  }

  it should "catch all exceptions" in {
    try {
      throw new IllegalStateException
    } catch {
      case _ => println("Catched")
    }
    succeed
  }

}
