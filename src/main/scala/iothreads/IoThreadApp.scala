package org.example.testsscala
package iothreads

import cats.effect.{IO, IOApp, IOLocal}
import cats.syntax.traverse._

import java.util.UUID
import scala.concurrent.duration.DurationInt

object IoThreadApp extends IOApp.Simple {

  // override protected def runtime: IORuntime = {
  //   var runtime: IORuntime = null
  //
  //   IOLocal()
  //
  //   val (compute, _) = IORuntime.createDefaultComputeThreadPool(runtime, threadPrefix = "mypool")
  //   val (blocking, _) = IORuntime.createDefaultBlockingExecutionContext()
  //   val (scheduler, _) = IORuntime.createDefaultScheduler()
  //
  //   runtime = IORuntime(compute, blocking, scheduler, () => (), IORuntimeConfig())
  //
  //   runtime
  // }

  override def run: IO[Unit] = {
    for {
      correlationId <- IOLocal(UUID.randomUUID())
      _ <- log("Hey mon pote")
      _ <- IO.sleep(500.milliseconds)
      _ <- log("La forme ?")
      _ <- startInThread()
    } yield ()
  }

  private val correlationId: ThreadLocal[String] = ThreadLocal.withInitial(() => "")

  private def log(message: String): IO[Unit] =
    IO.defer(IO.println(s"[${Thread.currentThread().getName} / CorrID = ${correlationId.get()}] $message"))

  private def initCorrelationId: IO[String] = {
    IO {
      val newCorrelationId = UUID.randomUUID().toString.replaceAll("-", "");
      correlationId.set(newCorrelationId)
      newCorrelationId
    }
  }

  private def startInThread(): IO[Unit] = {
    for {
      fiber1 <- log("01 - This must run in some thread").start
      fiber2 <- log("02 - This must run in some thread").start
      _ <- initCorrelationId
      fiber3 <- log("03 - This must run in some thread").start
      fiber4 <- log("04 - This must run in some thread").start
      fiber5 <- log("05 - This must run in some thread").start
      _ <- log("06 - This is the current thread")
      _ <- log("07 - This is the current thread")
      result <- Seq(fiber1, fiber2, fiber3, fiber4, fiber5).map(_.join).sequence
      x <- fiber1.join
      _ <- IO.println(s"Result is: $result")
    } yield ()
  }

}
