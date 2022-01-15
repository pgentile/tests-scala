package org.example.testsscala
package exceptions

import org.scalatest.flatspec.AnyFlatSpec

import scala.util.control.NonFatal

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
      case NonFatal(_) => println("Non fatal caught")
      case _ => println("Catched")
    }
    succeed
  }

}
