package org.example.testsscala
package functionalprogramming

object CaseMain {

  def main(args: Array[String]): Unit = {
    val caseEvenFunction: PartialFunction[Int, Int] = {
      case x: Int if x % 2 == 0 => x
      case x: Int if x == 3 => x
    }

    caseEvenFunction(2)
    caseEvenFunction(3)
    caseEvenFunction(4)
    println(caseEvenFunction.apply(6))
    println(caseEvenFunction.isDefinedAt(1))
    // println(Seq(1, 2, 3, 4, 5).map(caseEvenFunction))
  }


}
