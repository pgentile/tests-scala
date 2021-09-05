package org.example.testsscala
package validation

case class Unvalidated[A](private val value: A) {

  override def toString: String = s"Unvalidated($value)"

  def validate(): Validation[A] = Right(value)

}
