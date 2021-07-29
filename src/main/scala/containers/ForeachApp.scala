package org.example.testsscala
package containers

import scala.math.pow
import scala.util.Random

object ForeachApp extends App {

  implicit class IntExt(number: Int) {

    def *****(x: Int): Int = pow(number, x).toInt

  }

  IndexedSeq(1, 2, 3, 4).foreach { item =>
    println(s"Hello n°$item")
  }

  1 to 3 foreach { item =>
    println(s"Salut n°$item")
  }

  println(5 ***** 6)

  lazy val printItem: Unit = { println(s"Item ${Random.nextInt()}") }
  printItem
  printItem
}
