package org.example.testsscala
package workwithimplicits

import scala.concurrent.{ExecutionContext, Future}

object ImplicitApp extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val app = new ImplicitApp
  app.sayHello("Cooper")
  app.sayHello("Günther")

}

protected class ImplicitApp {

  def sayHello(name: String)(implicit ec: ExecutionContext): Future[Unit] =
    Future(printWithThreadName(s"Bonjour, $name.")).flatMap(_ => howDoYouDo)

  private def howDoYouDo(implicit ec: ExecutionContext): Future[Unit] =
    Future(printWithThreadName("Comment vas-tu ?"))

  private def printWithThreadName(value: String): Unit =
    println(s"[${Thread.currentThread.getName}] $value")

}
