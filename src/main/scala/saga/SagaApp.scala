package org.example.testsscala
package saga

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.Random

object SagaApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val saga = for {
      _ <- Saga.sideEffect(
        IO.println("OK 1"),
        IO.println("Rollback 1")
      )
      _ <- Saga.sideEffect(
        IO.println("OK 2"),
        IO.println("Rollback 2")
      )
      _ <- Saga.sideEffect(
        IO(Random.nextInt(100)).flatTap(n => IO.println(s"OK 3, random = ${n}")),
        (n: Int) => IO.println(s"Rollback 3 for ${n}")
      )
      fail <- Saga.noSideEffect(IO(Random.nextBoolean()))
      _ <- Saga.sideEffect(
        IO.unit.flatMap { _ =>
          if (fail) {
            IO.raiseError(new IllegalStateException("It must fail 4"))
          } else {
            IO.println("OK 4")
          }
        },
        IO.println("Rollback 4")
      )
      _ <- Saga.sideEffect(
        IO.println("OK 5"),
        IO.println("Rollback 5")
      )
      _ <- Saga.sideEffect[Unit](
        IO.raiseError(new IllegalStateException("It must fail 6")),
        IO.raiseError((new IllegalStateException("Rollback 6 fail")))
      )
    } yield ()

    saga.execute()
      .flatTap { result => IO.println(s"Result = $result") }
      .flatMap { result =>
        result.fold(
          (result, _) => IO.println(s"Success with $result"),
          (exception, rollback) => IO.println(s"Got error: $exception") *> rollback
        )
      }
      .handleErrorWith { e =>
        IO.println(s"Ohhh, error: $e") *> IO.pure(ExitCode.Error)
      }
      .as(ExitCode.Success)
  }

}
