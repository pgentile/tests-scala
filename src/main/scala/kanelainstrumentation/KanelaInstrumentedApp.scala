package org.example.testsscala
package kanelainstrumentation

import cats.effect.{IO, IOLocal}
import cats.effect.unsafe.{FiberMonitor, IORuntime, IORuntimeConfig}
import kamon.Kamon
import kamon.context.Context

import java.util.UUID
import java.util.concurrent.Executors
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContext, Future}

object KanelaInstrumentedApp {

  val correlationId: Context.Key[String] = Context.key("CorrelationId", "")

  def main(args: Array[String]): Unit = {
    Kamon.init()

    Kamon.runWithContext(Context.of(correlationId, "CID-" + UUID.randomUUID().toString)) {
      // runFuture()
      // runWithExecutorService()
      // runWithExecutorServiceRaw()
      runIO()

      log(s"correlationId       = ${Kamon.currentContext().get(correlationId)}")
    }
  }

  private def runFuture(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val future = for {
      _uuid1 <- Future(UUID.randomUUID())
      _ = log(s"F1: ${Kamon.currentContext().get(correlationId)}")
      _uuid2 <- Future(UUID.randomUUID())
      _ = log(s"F2: ${Kamon.currentContext().get(correlationId)}")
    } yield Kamon.currentContext().get(correlationId)

    Await.result(future, Duration.Inf)
  }

  private def runWithExecutorService(): Unit = {
    val executorService = Executors.newCachedThreadPool()
    implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutorService(executorService)

    val future = for {
      _uuid1 <- Future(UUID.randomUUID())
      _ = log(s"F3: ${Kamon.currentContext().get(correlationId)}")
      _uuid2 <- Future(UUID.randomUUID())
      _ = log(s"F4: ${Kamon.currentContext().get(correlationId)}")
    } yield Kamon.currentContext().get(correlationId)

    Await.result(future, Duration.Inf)
  }


  private def runWithExecutorServiceRaw(): Unit = {
    val executorService = Executors.newCachedThreadPool()

   val fa =  executorService.submit(new Runnable {
      override def run(): Unit = log(s"F5: ${Kamon.currentContext().get(correlationId)}")
    })

    val fb = executorService.submit(new Runnable {
      override def run(): Unit = log(s"F6: ${Kamon.currentContext().get(correlationId)}")
    })

    fa.get()
    fb.get()
  }

  private def runIO(): Unit = {
    import cats.effect.unsafe.implicits.global

    // val compute = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
    // val blocking = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
    // val (scheduler, _) = IORuntime.createDefaultScheduler()
    // implicit val ioRuntime: IORuntime = IORuntime(compute, blocking, scheduler, () => (), IORuntimeConfig())

    val context = Kamon.currentContext()
    val io = for {
      ctx <- IOLocal(context)
      requestId <- IOLocal(s"R-${UUID.randomUUID()}")
      logState = logCurrentState(_, requestId)
      f1 <- logState("F8.0").start
      _ <- IO.blocking(Thread.sleep(10))
      _ <- logState("F7.1") *> logState("F7.2")
      f2 <- logState("F8.1").start
      f3 <- logState("F8.2").start
      _ <- IO.cede
      _ <- logState("F9").start
      _ <- IO.sleep(10.milliseconds)
      _ <- logState("F10")
      _ <- logState("F11").start
      _ <- f1.join
      _ <- f2.join
      _ <- f3.join
    } yield ()

    io.unsafeRunSync()
  }

  private def log(s: String): Unit = println(s"[${Thread.currentThread().getName}] $s")

  private def logCurrentState(s: String, ioRid: IOLocal[String]): IO[Unit] = {
    val cid = Kamon.currentContext().get(correlationId)

    ioRid.get.flatMap(rid => IO(println(s"[${Thread.currentThread().getName}] (CID = $cid, RID = $rid) $s")))
  }

}
