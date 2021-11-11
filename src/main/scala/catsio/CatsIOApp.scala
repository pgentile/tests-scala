package org.example.testsscala
package catsio

import cats.effect.{ContextShift, IO}

import scala.concurrent.ExecutionContext
import scala.util.Random

object CatsIOApp extends App {

  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val hello = IO {
    val threadName = Thread.currentThread.getName
    val visitorId = Random.nextInt(1000)
    println(s"[$threadName] Hello, visitor #$visitorId")
    visitorId
  }

  val aboutVisitor = (visitorId: Int) => IO { println(s"This is visitor #$visitorId") }

  val error = IO.raiseError(new IllegalStateException("Fuck"))

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

  val result: Int = program
    // .handleErrorWith { error =>
    //   IO(println(s"Got some error: $error")) *> IO.pure(-1)
    // }
    .unsafeRunSync()
  println(result)

}
