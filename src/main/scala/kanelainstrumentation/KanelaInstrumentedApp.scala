package org.example.testsscala
package kanelainstrumentation

import cats.effect.{IO, IOLocal, Resource, ResourceIO}
import cats.effect.implicits._
import kamon.Kamon
import kamon.context.{Context, Storage}

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
      requestId <- IOLocal(s"aaa")
      logState = logCurrentState(_, requestId)
      f0 <- logState("F5").start
      _ <- logState("F6")

      _ <- withContext(context).use { c =>
        for {
          _ <- logState("F7")
          f1 <- logState("F8.0").start
          _ <- requestId.set("bbb")
          _ <- IO.blocking(Thread.sleep(10))
          _ <- logState("F9.1") *> logState("F9.2")

          _ <- withContext(c.withEntry(correlationId, "xxx")).use { _ =>
            for {
              _ <- logState("F9.2")
              f2 <- logState("F10").start
              f3 <- logState("F11").start
              //_ <- IO.cede
              _ <- logState("F12").start
              _ <- IO.sleep(10.milliseconds)
              _ <- logState("F13.1") *> logState(s"F13.2 current context is = $context")
              _ <- logState("F14").start
              _ <- f0.join
              _ <- f1.join
              _ <- f2.join
              _ <- f3.join
            } yield ()
          }

          _ <- logState("F15")
        } yield ()
      }

      _ <- logState("F15")
      _ <- logState("F16")
    } yield ()

    io.unsafeRunSync()
  }

  private def log(s: String): Unit = println(s"[${Thread.currentThread().getName}] $s")

  private def logCurrentState(s: String, ioRid: IOLocal[String]): IO[Unit] = {
    ioRid.get.flatMap { rid =>
      IO {
        val context = Kamon.currentContext()
        val cid = context.get(correlationId)
        println(s"[${Thread.currentThread().getName}] (CID = $cid, RID = $rid) $s")
      }
    }
  }

  private def withContext(currentContext: Context): ResourceIO[Context] = {
    for {
      localContext <- Resource.eval(IOLocal(Context.Empty))
      _ <- Resource.make(localContext.set(currentContext))(_ => localContext.reset)
      scope <- Resource.make(IO(Kamon.storeContext(currentContext)))(scope => IO(scope.close()))
      _ <- Resource.eval(IO(println(s"[${Thread.currentThread().getName}] Context set as resource: $currentContext")))
    } yield scope.context
  }

}
