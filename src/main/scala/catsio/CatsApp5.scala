package org.example.testsscala
package catsio

import cats.effect.{IO, IOApp}
import kamon.Kamon

import java.util.UUID

object CatsApp5 extends IOApp.Simple {

  override def run: IO[Unit] = {
    IO.async_[String] { callback: (Either[Throwable, String] => Unit) =>
      Kamon.storeContext(Kamon.currentContext().withTag("toto", "tutu"))

      logCurrentStack(s"01 -- async")
      logKamonContext()

      new Thread(() => {
        logCurrentStack(s"02 -- callback")
        logKamonContext()

        Thread.sleep(500)
        callback(Right("Okay !"))
      }).start()

      ()
    }.map { _ =>
      logCurrentStack(s"03 -- map")
      logKamonContext()
    }

    /*
    IO(UUID.randomUUID())
      .flatMap { id =>
        logCurrentStack(s"01 -- flatMap -- ID is $id")
        IO.unit
      }
      .flatMap { _ =>
        IO(Kamon.storeContext(Kamon.currentContext().withTag("toto", "tutu")))
      }
      .map { _ =>
        logCurrentStack(s"02 -- map")
        logKamonContext()
        ()
      }
      .flatMap { _ =>
        logCurrentStack(s"03 -- flatMap before creating fiber")

        IO(logCurrentStack(s"04 -- in a fiber"))
          .start
          .flatMap { fiber =>
            fiber.join
          }
          .map { _ =>
            logCurrentStack(s"05 -- map after join")
            ()
          }
      }
      .flatMap { _ =>
        IO.cede
      }
      .map { _ =>
        logCurrentStack(s"06 -- map after cede")
        logKamonContext()
        ()
      }
     */
  }

  private def logCurrentStack(message: String): Unit = {
    val thread = Thread.currentThread()

    println(s"[$thread] $message")

    // Drop getStackTrace method and  logCurrentStack method stack elements
    thread.getStackTrace.drop(2).foreach { stackTraceElement =>
      println(s"- $stackTraceElement")
    }

    println()
  }

  private def logKamonContext(): Unit = {
    val thread = Thread.currentThread()

    println(s"[$thread] Kamon context: ${Kamon.currentContext()}")

  }

}
