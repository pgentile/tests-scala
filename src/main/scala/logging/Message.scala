package org.example.testsscala
package logging

sealed trait Message {

  def format: String

  def args: Seq[Any]

}

object Message {

  def apply(s: String): Message = SimpleMessage(s)

  def apply(sc: StringContext, args: Seq[Any]): Message = {
    if (sc.parts.length == 1) {
      SimpleMessage(sc.parts.head)
    } else {
      ComplexMessage(sc, args)
    }
  }
}

case class SimpleMessage private(private val s: String) extends Message {

  override def format: String = s

  override def args: Seq[Any] = Nil

}

case class ComplexMessage private(private val sc: StringContext, private val _args: Seq[Any]) extends Message {

  def format: String = sc.parts.mkString("{}")

  override def args: Seq[Any] = _args

}
