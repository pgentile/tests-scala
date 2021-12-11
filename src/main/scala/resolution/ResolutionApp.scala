package org.example.testsscala
package resolution

import cats.syntax._
import cats.implicits._

object ResolutionApp {

  def main(args: Array[String]): Unit = {
    val resolutionFactory = Resolution.init[Id, Toto](toto => Set(toto.id)) _
    val result = resolutionFactory(Set(Id("A"), Id("B"), Id("C"))).resolveAll[Option] { id =>
      Some(Toto(id))
    }
    result.foreach(println)
  }

  case class Id(value: String)

  case class Toto(id: Id)

}
