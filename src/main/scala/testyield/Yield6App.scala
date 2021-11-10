package org.example.testsscala
package testyield

import scala.util.{Success, Try}

import scala.language.implicitConversions

object Yield6App extends App {

  implicit def try2option[T](t: Try[T]): Option[T] = t.toOption

  val results = for {
    item <- Seq(1, 2, 3)
    itemx2 = item * 2
    value <- Some("value")
    value2 <- Some("y")
    value3 <- Success("abc")
  } yield s"$item-$itemx2-$value-$value2-$value3"

  println(s"Results are: $results")

}
