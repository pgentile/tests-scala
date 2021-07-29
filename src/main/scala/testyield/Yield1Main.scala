package org.example.testsscala
package testyield

object Yield1Main {

  def main(args: Array[String]): Unit = {
    val firstNames = List("Julie", "Pierre", "Cooper")
    val lastNames = List("Gentile", "Hernandez")

    val names = for (firstName <- firstNames; lastName <- lastNames) yield firstName + " " + lastName
    println(names.mkString(", "))
  }

}
