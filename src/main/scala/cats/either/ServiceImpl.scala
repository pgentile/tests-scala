package org.example.testsscala
package cats.either


import _root_.cats.Functor
import _root_.cats.data.EitherT
import _root_.cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

object ServiceImpl extends Service {

  implicit private val ec: ExecutionContext = ExecutionContext.global

  override def compute(id: String): Future[Either[FunctionalError, ServiceResult]] = {
    EitherT.right[FunctionalError](Future.successful(id))
      .flatMapF { someId =>
        computeResult(someId)
          .map(Either.right)
          .recover {
            case _: Exception => Either.left(BadDateError)
          }
      }
      .value
  }

  private def computeResult(id: String): Future[ServiceResult] = Future {
    if (id.startsWith("A")) ServiceResult else throw new RuntimeException
  }

}

class FutureFunctor(implicit ec: ExecutionContext) extends Functor[Future] {

  override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)
}
