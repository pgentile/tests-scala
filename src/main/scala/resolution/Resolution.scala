package org.example.testsscala
package resolution

import cats.{Applicative, Monad}
import cats.implicits._

final class Resolution[R, T] private(private val refExtractor: T => Set[R], private val unresolved: Set[R], private val resolved: Map[R, T]) {

  def allResolved: Boolean = unresolved.isEmpty

  def addResolved(item: T): Resolution[R, T] = {
    val extractedRefs = refExtractor(item)
    new Resolution(
      refExtractor,
      unresolved.diff(extractedRefs),
      resolved ++ extractedRefs.map(_ -> item)
    )
  }

  def resolveOne[F[_]](getItem: R => F[T])(implicit F: Applicative[F]): F[Resolution[R, T]] = {
    unresolved.headOption
      .map { ref =>
        val item = getItem(ref)
        F.map(item)(addResolved)
      }
      .getOrElse(F.pure(this))
  }

  def resolveAll[F[_]](getItem: R => F[T])(implicit F: Monad[F]): F[Resolution[R, T]] = {
    resolveOne(getItem).flatMap { resolution =>
      if (resolution.allResolved) {
        F.pure(resolution)
      } else {
        resolution.resolveOne(getItem)
      }
    }

    F.flatMap(resolveOne(getItem)) { resolution =>
      if (resolution.allResolved) {
        F.pure(resolution)
      } else {
        resolution.resolveOne(getItem)
      }
    }
  }

}

object Resolution {

  def init[R, T](refExtractor: T => Set[R])(unresolved: Set[R]): Resolution[R, T] = new Resolution(refExtractor, unresolved, Map.empty)

}
