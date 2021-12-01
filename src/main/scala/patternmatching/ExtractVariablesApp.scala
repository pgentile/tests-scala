package org.example.testsscala
package patternmatching

object ExtractVariablesApp extends App {

  val Some(value) = Some("name")
  println(s"value = $value")

  val option: Option[String] = None
  val Some(value2: String) = option
  println(s"value2 = $value2")

}
