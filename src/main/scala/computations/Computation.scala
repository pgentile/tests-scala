package org.example.testsscala
package computations

class Computation[A] private (f: () => A) {

  private val fn: () => A = f

  def map[B](f: A => B): Computation[B] = Computation(f(fn()))

  def flatMap[B](f: A => Computation[B]): Computation[B] = f(fn())

  def execute(): A = fn()

}

object Computation {

  def apply[A](f: => A): Computation[A] = new Computation[A](() => f)

}
