package org.example.testsscala
package amount

object AmountApp extends App {

  import Helpers._

  val a1 = 12.euros
  val a2 = 3.euros
  val a3 = 1.euro

  val amounts = List(
    1.euro,
    0.euros,
    15.euros,
    -12.euros,
    18.6.euros
  )

  println(s"a1 + a2 = ${a1 + a2}")
  println(s"a1 - a2 = ${a1 - a2}")
  println(s"-a2 = ${-a2}")
  println(s"a1 * 3 = ${a1 * 3}")
  println(s"a3 = $a3")
  println(s"a3 = $a3")
  println(s"sum of amounts = ${amounts.reduce(_ + _)}")

  amounts.foreach {
    case Amount(value, EUR) if value == 0 => println("Zero €")
    case Amount(value, USD) if value == 0 => println("Zero $")
    case Amount(value, EUR) => println(s"$value €")
    case Amount(value, currency) => println(s"$value ${currency.symbol}")
    case _ => println("Other")
  }

}
