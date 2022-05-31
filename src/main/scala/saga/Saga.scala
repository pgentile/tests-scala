package org.example.testsscala
package saga

import cats.data.EitherT
import cats.effect.{ExitCode, IO, IOApp}

import scala.util.Random

sealed trait SagaFailure
case object SagaRolledBack extends SagaFailure
case object SagaPartiallyRolledBack extends SagaFailure

trait Saga[A] {

  import Saga._

  def map[B](f: A => B): Saga[B] = flatMap(value => pure(f(value)))

  def flatMap[B](f: A => Saga[B]): Saga[B] = FlatMap(this, f)

  def execute(): IO[Either[SagaFailure, A]]

}

object Saga {

  def pure[A](value: A): Saga[A] = Pure(value)

  def unit: Saga[Unit] = pure(())

  def sideEffect[A](io: IO[A]): SideEffect[A] = SideEffect(io)

  def noSideEffect[A](io: IO[A]): NoSideEffect[A] = NoSideEffect(io)

  def rollbackable[A](io: IO[A], rollback: IO[Unit]): RollbackableSideEffect[A] = RollbackableSideEffect(io, rollback)

  case class Pure[A](value: A) extends Saga[A] {

    override def execute(): IO[Either[SagaFailure, A]] = IO.pure(Right(value))

  }

  case class FlatMap[A, B](input: Saga[A], f: A => Saga[B]) extends Saga[B] {

    override def execute(): IO[Either[SagaFailure, B]] = {
      val either: EitherT[IO, SagaFailure, B] = for {
        inputValue <- EitherT(input.execute())
        outputValue <- EitherT(f(inputValue).execute())
      } yield outputValue

      either.value
    }

  }

  case class SideEffect[A](io: IO[A]) extends Saga[A] {

    override def execute(): IO[Either[SagaFailure, A]] = {
      io
        .map(Right(_))
        .handleErrorWith { _ => IO(Left(SagaPartiallyRolledBack)) }
    }

  }

  case class NoSideEffect[A](io: IO[A]) extends Saga[A] {

    override def execute(): IO[Either[SagaFailure, A]] = {
      io
        .map(Right(_))
        .handleErrorWith { _ => IO(Left(SagaRolledBack)) }
    }

  }

  case class RollbackableSideEffect[A](io: IO[A], compensation: IO[Unit]) extends Saga[A] {

    override def execute(): IO[Either[SagaFailure, A]] = {
      io
        .map(Right(_))
        .handleErrorWith { _ =>
          compensation
            .as(Left(SagaRolledBack))
            .handleError { _ => Left(SagaPartiallyRolledBack) }
        }
    }

  }

}

object SagaApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val saga = for {
      _ <- Saga.rollbackable(
        IO.println("OK 1"),
        IO.println("Rollback 1")
      )
      _ <- Saga.rollbackable(
        IO.println("OK 2"),
        IO.println("Rollback 2")
      )
      _ <- Saga.rollbackable(
        IO.println("OK 3"),
        IO.println("Rollback 3")
      )
      fail <- Saga.noSideEffect(IO(Random.nextBoolean() || true))
      _ <- Saga.rollbackable(
        IO.unit.flatMap { _ =>
          if (fail) {
            IO.raiseError(new IllegalStateException("It must fail"))
          } else {
            IO.println("OK 4")
          }
        },
        IO.println("Rollback 4")
      )
      _ <- Saga.rollbackable(
        IO.println("OK 5"),
        IO.println("Rollback 5")
      )
    } yield ()

    saga.execute()
      .flatMap { result =>
        IO.println(s"Result = $result")
      }
      .map(_ => ExitCode.Success)
  }

}
