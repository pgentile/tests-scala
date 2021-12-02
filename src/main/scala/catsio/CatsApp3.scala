package org.example.testsscala
package catsio

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration._
import scala.util.Random

object CatsApp3 extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      number <- IO.blocking(Random.nextInt(100))
      _ <- IO.defer {
        IO.println(s"Got number $number").delayBy(10.seconds)
      }
    } yield ExitCode.Success
  }

}
