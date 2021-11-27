package org.example.testsscala
package doublemonad

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object OptionFutureApp extends App {

  def traverse[A](of: Option[Future[A]])(implicit ec: ExecutionContext): Future[Option[A]] = {
    of.fold[Future[Option[A]]](Future.successful(None))(f => f.map(Some(_)))
  }

  {
    import ExecutionContext.Implicits.global

    val name = "Pierre"

    val fHello = traverse(Some(Future {
      println(s"[${Thread.currentThread.getName}] Hello, $name")
      name
    }))

    val oOutput = Await.result(fHello, Duration.Inf)
    oOutput.foreach(item => println(s"Item = $item"))
  }

}
