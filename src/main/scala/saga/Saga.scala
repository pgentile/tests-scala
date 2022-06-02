package org.example.testsscala
package saga

import cats.effect.IO

sealed trait SagaExecutionResult[+A] {

  def fold[B](onSuccess: (A, IO[Unit]) => B, onFailure: (Throwable, IO[Unit]) => B): B

}

object SagaExecutionResult {

  case class Success[+A](value: A, rollback: IO[Unit]) extends SagaExecutionResult[A] {

    override def fold[B](onSuccess: (A, IO[Unit]) => B, onFailure: (Throwable, IO[Unit]) => B): B = onSuccess(value, rollback)

  }

  case class Failure[+A](exception: Throwable, rollback: IO[Unit]) extends SagaExecutionResult[A] {

    override def fold[B](onSuccess: (A, IO[Unit]) => B, onFailure: (Throwable, IO[Unit]) => B): B = onFailure(exception, rollback)

  }

}


sealed trait SagaFailure

case object SagaRolledBack extends SagaFailure

case object SagaPartiallyRolledBack extends SagaFailure

trait Saga[A] {

  import Saga._

  def map[B](f: A => B): Saga[B] = flatMap(value => pure(f(value)))

  def flatMap[B](f: A => Saga[B]): Saga[B] = FlatMap(this, f)

  def execute(): IO[SagaExecutionResult[A]]

}

object Saga {

  def pure[A](value: A): Saga[A] = Pure(value)

  def unit: Saga[Unit] = pure(())

  def sideEffect[A](io: IO[A], rollbackOnSuccess: A => IO[Unit]): SideEffect[A] =
    SideEffect(io, Some(rollbackOnSuccess), None)

  def sideEffect[A](io: IO[A], rollbackOnSuccess: A => IO[Unit], rollbackOnFailure: IO[Unit]): SideEffect[A] =
    SideEffect(io, Some(rollbackOnSuccess), Some(rollbackOnFailure))

  def sideEffect[A](io: IO[A], rollback: IO[Unit]): SideEffect[A] = sideEffect(io, _ => rollback, rollback)

  def noSideEffect[A](io: IO[A]): NoSideEffect[A] = NoSideEffect(io)

  case class Pure[A](value: A) extends Saga[A] {

    import SagaExecutionResult._

    override def execute(): IO[SagaExecutionResult[A]] =
      IO.pure(Success(value, IO.unit))

  }

  case class FlatMap[A, B](input: Saga[A], f: A => Saga[B]) extends Saga[B] {

    import SagaExecutionResult._

    override def execute(): IO[SagaExecutionResult[B]] = {
      for {
        result1 <- input.execute()
        result2 <- result1.fold(
          (value1, rollback1) => f(value1).execute().map { result2 =>
            result2.fold(
              (value2, rollback2) => Success(value2, rollback2 *> rollback1),
              (exception, rollback2) => Failure(exception, rollback2 *> rollback1)
            )
          },
          (exception, rollback) => IO.pure(Failure[B](exception, rollback))
        )
      } yield result2
    }

  }

  case class SideEffect[A](io: IO[A], rollbackOnSuccess: Option[A => IO[Unit]], rollbackOnFailure: Option[IO[Unit]]) extends Saga[A] {

    import SagaExecutionResult._

    override def execute(): IO[SagaExecutionResult[A]] = {
      io
        .map { output =>
          val rollback: IO[Unit] = rollbackOnSuccess
            .map { fr => fr(output) }
            .getOrElse(IO.unit)

          Success(output, rollback)
        }
        .handleError { e =>
          val rollback: IO[Unit] = rollbackOnFailure.getOrElse(IO.unit)

          Failure(e, rollback)
        }
    }

  }

  case class NoSideEffect[A](io: IO[A]) extends Saga[A] {

    override def execute(): IO[SagaExecutionResult[A]] = {
      io
        .map { output => SagaExecutionResult.Success(output, IO.unit) }
        .handleError { e => SagaExecutionResult.Failure(e, IO.unit) }
    }

  }

}
