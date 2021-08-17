package org.example.testsscala
package functionalprogramming

object CaseApp extends App {

  val caseEvenFunction: PartialFunction[Int, Int] = {
    case x if x % 2 == 0 => x
    case x if x == 3 => x
  }

  caseEvenFunction(2)
  caseEvenFunction(3)
  caseEvenFunction(4)
  println(caseEvenFunction.apply(6))
  println(caseEvenFunction.isDefinedAt(1))
  (0 to 10).foreach { n => println(s"$n => ${caseEvenFunction.isDefinedAt(n)}") }
}
