package org.example.testsscala
package people

case class Name(firstName: String, lastName: String) extends Ordered[Name] {

  override def compare(that: Name): Int = Name.ordering.compare(this, that)

  override def toString: String = s"$firstName $lastName"

}

object Name {

  private val ordering = Ordering.by[Name, String](_.lastName).thenComparing(_.firstName)

}
