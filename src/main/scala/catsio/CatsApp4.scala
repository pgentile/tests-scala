package org.example.testsscala
package catsio

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration._
import scala.util.Random

object CatsApp4 extends IOApp.Simple {

  override def run: IO[Unit] = {
    val loop = (for {
      counterRef <- IO.ref(1)
      _ <- (for {
        counter <- counterRef.getAndUpdate(_ + 1)
        _ <- IO.println(s"Bonjour, toi n°$counter")
        _ <- IO.sleep(2.seconds)
      } yield ()).foreverM
    } yield ())

    loop.onCancel(IO.println("Cancelled"))
  }

}
