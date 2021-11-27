package org.example.testsscala
package doublemonad

import scala.util.{Failure, Success, Try}

object OptionTryApp extends App {

  def traverse[A](ot: Option[Try[A]]): Try[Option[A]] = {
    ot.fold[Try[Option[A]]](Success(None))(t => t.map(Some(_)))
  }

  def traverse[A](to: Try[Option[A]]): Option[Try[A]] = {
    to.fold(
      e => Some(Failure(e)),
      opt => opt.map(Success(_))
    )
  }

}
