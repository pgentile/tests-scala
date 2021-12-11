package org.example.testsscala
package resolution

final class RefResolution[R, T] private(private val refExtractor: T => Set[R], private val unresolved: Set[R], private val resolved: Map[R, T]) {

  def unresolvedRef: Option[R] = unresolved.headOption

  def getResolved: Seq[T] = resolved.values.toSeq

  def allResolved: Boolean = unresolved.isEmpty

  def resolve(item: T): RefResolution[R, T] = {
    val extractedRefs = refExtractor(item)
    new RefResolution(
      refExtractor,
      unresolved.diff(extractedRefs),
      resolved ++ extractedRefs.map(_ -> item)
    )
  }

}

object RefResolution {

  def init[R, T](refExtractor: T => Set[R])(unresolved: Set[R]): RefResolution[R, T] = new RefResolution(refExtractor, unresolved, Map.empty)

}
