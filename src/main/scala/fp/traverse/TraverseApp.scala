package org.example.testsscala
package fp.traverse

import cats.syntax.all._

import scala.util.Try
import scala.math.Numeric._

object TraverseApp extends App {

  private def parseInt(item: String): Try[Int] = Try(item.toInt)

  private def onlyEvenNumbers[T](number: T)(implicit numeric: Integral[T]): Option[T] = {
    if (numeric.rem(number, numeric.fromInt(2)) == numeric.zero) Some(number) else None
  }

  {
    val numbers = Seq("1", "2", "3")
    val result: Try[Seq[Int]] = numbers.traverse(parseInt)
    println(result)
  }

  {
    val numbers = Seq("1", "deux", "3")
    val result: Try[Seq[Int]] = numbers.traverse(parseInt)
    println(result)
  }

  {
    val numbers = Seq("1", "2", "3").as(Seq(1, 2))
    println(numbers)
  }

  {
    val numbers: Seq[Int] = (1 to 10)
    val result: Option[Seq[Int]] = numbers.traverse(onlyEvenNumbers(_))
    println(result)
  }

}
