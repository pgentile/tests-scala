package org.example.testsscala
package testyield

import scala.util.{Failure, Success}

object Yield3Main {

  def main(args: Array[String]): Unit = {
    val firstName = Success("Pierre")
    val lastName = Failure[String](new RuntimeException("Failed"))

    val name = for (fn <- firstName if fn.startsWith("P"); ln <- lastName) yield fn + " " + ln
    println(name)
  }

}
