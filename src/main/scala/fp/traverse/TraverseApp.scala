package org.example.testsscala
package fp.traverse

import cats.syntax.all._

import scala.util.Try

object TraverseApp extends App {

  private def parseInt(item: String): Try[Int] = Try(item.toInt)

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

}
