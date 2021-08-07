package org.example.testsscala
package privacy

final class Privacy[+A] private(private val value: A, private[this] val reprAnonymizer: Anonymizer[A, String]) {

  def untaint: A = value

  def map[B](f: A => B): Privacy[B] = Privacy[B](f(value))

  def flatMap[B](f: A => Privacy[B]): Privacy[B] = f(value)

  def matches(p: A => Boolean): Boolean = p(value)

  def anonymize[B](anonymizer: Anonymizer[A, B]): B = anonymizer(value)

  def withReprAnonymizer(anonymizer: Anonymizer[A, String]): Privacy[A] = Privacy(value, anonymizer)

  def withRepr(repr: String): Privacy[A] = Privacy(value, repr)

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: Privacy[_] => value == other.value
      case _ => false
    }
  }

  override def hashCode(): Int = value.hashCode()

  override def toString: String = anonymize(reprAnonymizer)

}

object Privacy {

  def apply[A](v: A, anonymizer: Anonymizer[A, String] = DefaultAnonymizer) = new Privacy[A](v, anonymizer)

  def apply[A](v: A, repr: String) = new Privacy[A](v, new ReplacementAnonymizer(repr))

}
