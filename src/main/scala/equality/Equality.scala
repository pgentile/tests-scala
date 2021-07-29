package org.example.testsscala
package equality

object Equality {

  def main(args: Array[String]): Unit = {
    val ref1 = CustomerReference("A")
    val ref2 = CustomerReference("A")

    println(s"ref1 == ref1: ${ref1 == ref1}")
    println(s"ref1 == ref2: ${ref1 == ref2}")

    println(s"ref1 eq ref1: ${ref1 eq ref1}")
    println(s"ref1 eq ref2: ${ref1 eq ref2}")
  }

}
