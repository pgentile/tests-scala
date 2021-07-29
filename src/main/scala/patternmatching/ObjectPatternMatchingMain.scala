package org.example.testsscala
package patternmatching

import scala.collection.immutable.LinearSeq

object ObjectPatternMatchingMain {

  case class Name(firstName: String, lastName: String) {

    override def toString = s"Name($firstName $lastName)"

  }

  def main(args: Array[String]): Unit = {
    val name1 = Name("Jean", "Bon")
    val name2 = Name("Guy", "Gnol")
    val name3 = Name("Debbie", "Loss")

    val result = name2 match {
      case Name("Jean", _) => "Jambon"
      case name @ Name("Guy", _) => s"Guignol ($name)"
    }

    println(result)
  }

}
