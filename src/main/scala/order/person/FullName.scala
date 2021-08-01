package org.example.testsscala
package order.person

case class FullName(firstName: Name, lastName: Name) {

  override def toString: String = s"${getClass.getSimpleName}(${firstName.value} ${lastName.value})"

}

object FullName {

  def apply(firstName: String, lastName: String): FullName = FullName(Name(firstName), Name(lastName))

}
