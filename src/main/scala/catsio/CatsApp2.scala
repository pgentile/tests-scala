package org.example.testsscala
package catsio

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration._

object CatsApp2 extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    IO.defer {
      println(s"[${Thread.currentThread.getName}] Debug IO #1")
      IO.println(s"[${Thread.currentThread.getName}] Hello, my friend")
    } *> IO.defer {
      println(s"[${Thread.currentThread.getName}] Debug IO #2")
      IO.println(s"[${Thread.currentThread.getName}] How do you do?")
    } *> IO.sleep(2.seconds) *> IO.defer {
      println(s"[${Thread.currentThread.getName}] Debug IO #3")
      IO.sleep(5.seconds)
    } *> IO.pure(ExitCode.Success)
  }

}
