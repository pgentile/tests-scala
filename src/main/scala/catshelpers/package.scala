package org.example.testsscala

import scala.concurrent.Future

package object catshelpers {

  implicit class AsFutureOps[T](val value: T) extends AnyVal {

    def asFuture: Future[T] = Future.successful(value)

  }

  implicit class ThrowableAsFutureOps[T <: Throwable](val exception: T) extends AnyVal {

    def asFuture: Future[T] = Future.failed(exception)

  }

}
