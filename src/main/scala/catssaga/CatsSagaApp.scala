package org.example.testsscala
package catssaga

import cats.Applicative
import cats.effect.{ExitCode, IO, IOApp}
import com.vladkopanev.cats.saga.{Saga, SagaDefaultTransactor, SagaTransactor}
import com.vladkopanev.cats.saga.Saga._

import java.util.UUID
import scala.util.Random

object CatsSagaApp extends IOApp {

  private type SagaIO[A] = Saga[IO, A]

  private implicit val sagaTransactor: SagaTransactor[IO] = new SagaDefaultTransactor

  override def run(args: List[String]): IO[ExitCode] = {

    val saga: SagaIO[Unit] = for {
      conversationId <- createConversation.compensateIfSuccess(deleteConversation)

      _ <- joinConversation(conversationId).compensate(releaseConversation(conversationId))

      monitorConversation <- randomDouble.map(_ < 0.8).noCompensate
      _ <- Applicative[SagaIO].whenA(monitorConversation) {
        addMonitor(conversationId).compensateIfSuccess(cancelMonitor)
      }

      transactionId <- newRandomID("tx").noCompensate
      _ <- registerTransaction(transactionId, conversationId).compensate(removeTransaction(transactionId))
    } yield ()

    IO.println("Hey, run the saga !") *> saga.transact *> IO.println("Done with the saga") *> IO.pure(ExitCode.Success)
  }

  private def createConversation: IO[String] =
    IO.println("Creating conversation...") *> newRandomID("c")

  private def deleteConversation(id: String): IO[Unit] =
    IO.println(s"Deleting conversation $id")

  private def joinConversation(id: String): IO[Unit] =
    IO.println(s"Join conversation $id") <* randomFault("joinConversation", 0.1)

  private def releaseConversation(id: String): IO[Unit] =
    IO.println(s"Release conversation $id")

  private def registerTransaction(transactionId: String, conversationId: String): IO[Unit] =
    IO.println(s"Register transaction $transactionId for conversation $conversationId") <* randomFault("registerTransaction", 0.2)

  private def removeTransaction(transactionId: String): IO[Unit] =
    IO.println(s"Remove transaction $transactionId")

  private def addMonitor(conversationId: String): IO[String] =
    IO.println(s"Add monitor on $conversationId") *> randomFault("addMonitor", 0.3) *> newRandomID("m")

  private def cancelMonitor(id: String): IO[Unit] =
    IO.println(s"Cancel monitor $id")

  private def newRandomID(prefix: String): IO[String] =
    IO(prefix + "_" + UUID.randomUUID().toString.replaceAll("-", ""))

  private val randomDouble: IO[Double] = IO(Random.nextDouble())

  private def randomFault(name: String, probability: Double): IO[Unit] =
    randomDouble.flatMap { result =>
      if (result <= probability) {
        IO.raiseError(new IllegalStateException(s"Got a random fault $name"))
      } else {
        IO.unit
      }
    }

}
