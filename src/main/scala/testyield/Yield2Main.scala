package org.example.testsscala
package testyield

import scala.util.{Failure, Success}

object Yield2Main {

  def main(args: Array[String]): Unit = {
    val firstName = Some("Pierre")
    val lastName = None

    val name = for (fn <- firstName; ln <- lastName) yield fn + " " + ln
    println(name)
  }

}
