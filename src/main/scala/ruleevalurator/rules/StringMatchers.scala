package org.example.testsscala
package ruleevalurator.rules

object StringMatchers {

  def eqTo(value: String): String => Boolean = s => s == value

  def startsWith(value: String): String => Boolean = s => s.startsWith(value)

  def endsWith(value: String): String => Boolean = s => s.endsWith(value)

  def contains(value: String): String => Boolean = s => s.contains(value)

}
