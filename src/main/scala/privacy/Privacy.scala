package org.example.testsscala
package privacy

final case class Privacy[+A] private(private val value: A) {

  def untaint: A = value

  def map[B](f: A => B): Privacy[B] = Privacy[B](f(value))

  def flatMap[B](f: A => Privacy[B]): Privacy[B] = f(value)

  def matches(p: A => Boolean): Boolean = p(value)

  def anonymize[B](implicit anonymizer: Anonymizer[A, B]): B = anonymizer(value)

  override def toString: String = anonymize(DefaultAnonymizer)

}

object Privacy {

  private[this] def unapply[A](p: Privacy[A]): Option[String] = ???

}
