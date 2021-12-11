package org.example.testsscala
package resolution

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object ResolutionApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val resolution = new Resolution[Id, Toto](toto => Set(toto.id))

    val refs = Set(Id("A"), Id("B"), Id("C"))

    for {
      items <- resolution.resolve(refs) { id =>
        IO.pure(Toto(id))
      }
      _ <- IO.println(s"Items are: $items")
    } yield ExitCode.Success
  }

  case class Id(value: String)

  case class Toto(id: Id)

}
