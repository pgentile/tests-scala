package org.example.testsscala
package people

case class Name(firstName: String, lastName: String) extends Ordered[Name] {

  override def compare(that: Name): Int = Name.ordering.compare(this, that)

  override def toString: String = s"$firstName $lastName"

}

object Name {

  val orderingByLastName: Ordering[Name] = Ordering.by(_.lastName)
  val orderingByFirstName: Ordering[Name] = Ordering.by(_.firstName)

  implicit val ordering: Ordering[Name] = orderingByLastName.orElse(orderingByFirstName)

}
