package org.example.testsscala
package privacy

final class Privacy[+A](private val value: A) {

  def map[B](f: A => B): Privacy[B] = new Privacy[B](f(value))

  def flatMap[B](f: A => Privacy[B]): Privacy[B] = f(value)

  def matches(p: A => Boolean): Boolean = p(value)

  def anonymize[B](f: A => B): B = f(value)

  def untaint: A = value

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: Privacy[_] => value == other.value
      case _ => false
    }
  }

  override def hashCode(): Int = value.hashCode()

}

object Privacy {

  def apply[A](v: A) = new Privacy[A](v)

}
