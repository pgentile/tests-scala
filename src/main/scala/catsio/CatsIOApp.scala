package org.example.testsscala
package catsio

import cats.effect.{IO, IOApp}

import scala.util.Random

object CatsIOApp extends IOApp.Simple {

  private val hello = IO {
    val threadName = Thread.currentThread.getName
    val visitorId = Random.nextInt(1000)
    println(s"[$threadName] Hello, visitor #$visitorId")
    visitorId
  }

  private val aboutVisitor = (visitorId: Int) => IO { println(s"This is visitor #$visitorId") }

  private val error = IO.raiseError(new IllegalStateException("Fuck"))

  val program: IO[Int] = for {
    visitor1 <- hello
    _ <- aboutVisitor(visitor1)
    visitor2 <- hello
    _ <- aboutVisitor(visitor2)
    _ <- error.attempt.map(_.toTry)
    _ <- hello
    x: Int = if (visitor1 != visitor2) {
      throw new IllegalStateException("Fuck, les visiteurs sont différents")
    } else {
      52
    }
  } yield x

  override def run: IO[Unit] = program.as(())

}
