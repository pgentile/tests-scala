package org.example.testsscala
package testyield

import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}
import scala.reflect.ClassTag

object Yield4Main {

  def main(args: Array[String]): Unit = {
    val civility: Option[String] = None
    val firstName = Success("Pierre")
    val lastName = Some("Gentile")

    val name = for (fn <- firstName if fn.startsWith("P"); ln <- lastName; civ <- civility) yield civ + " " + fn + " " + ln
    println(name)
  }

  implicit def option2try[T](option: Option[T])(implicit clazz: ClassTag[T]): Try[T] = {
    option
      .map { value => Success(value) }
      .getOrElse {
        Failure[T](new NoSuchElementException(s"Missing value for Option[$clazz]"))
      }
  }

}
