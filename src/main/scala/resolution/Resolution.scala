package org.example.testsscala
package resolution

import cats.implicits._
import cats.{Applicative, Monad}

class Resolution[R, T](refExtractor: T => Set[R]) {

  private val refResolutionFactory = RefResolution.init(refExtractor) _

  def resolve[F[_]](refs: Set[R])(getItem: R => F[T])(implicit F: Monad[F]): F[Seq[T]] = {
    val refResolution = refResolutionFactory(refs)
    resolveMany(refResolution, getItem).map(_.getResolved)
  }

  private def resolveMany[F[_]](refResolution: RefResolution[R, T], getItem: R => F[T])(implicit F: Monad[F]): F[RefResolution[R, T]] = {
    resolveOne(refResolution, getItem).flatMap { newRefResolution =>
      if (newRefResolution.allResolved) {
        F.pure(newRefResolution)
      } else {
        resolveMany(newRefResolution, getItem)
      }
    }
  }

  private def resolveOne[F[_]](refResolution: RefResolution[R, T], getItem: R => F[T])(implicit F: Applicative[F]): F[RefResolution[R, T]] = {
    refResolution.unresolvedRef
      .map { ref =>
        val fItem = getItem(ref)
        fItem.map(refResolution.resolve)
      }
      .getOrElse(F.pure(refResolution))
  }

}
