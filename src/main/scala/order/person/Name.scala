package org.example.testsscala
package order.person

case class Name(value: String) {

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: Name => value.toLowerCase == other.value.toLowerCase
      case _ => false
    }
  }

  override def hashCode(): Int = value.toLowerCase.hashCode

}
