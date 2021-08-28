package org.example.testsscala
package functionalprogramming

object LiftApp extends App {

  type Pair[A] = (A, A)

  def lift[A, B](f: A => B): Pair[A] => Pair[B] = (pa: Pair[A]) => f(pa._1) -> f(pa._2)

  def liftSeq[A, B](f: A => B): Seq[A] => Seq[B] = (seq: Seq[A]) => seq.map(f)

  def liftMapKey[A, B, V](f: A => B): Map[A, V] => Map[B, V] = m => m.map { case (key, value) => f(key) -> value }

  def liftMapValue[A, B, K](f: A => B): Map[K, A] => Map[K, B] = m => m.map { case (key, value) => key -> f(value) }

  val multiplyByTwo: Int => Double = _ * 2.0
  val lifted = lift(multiplyByTwo)
  val result = lifted(3 -> -2)
  println(s"result = $result")

  val mapKeyLifted = liftMapKey(multiplyByTwo)
  val mapValueLifted = liftMapValue(multiplyByTwo)

  println(liftMapValue(multiplyByTwo)(Map("X" -> 2, "Y" -> 9, "A" -> 8)))

  Seq("A", "B").sorted

}
