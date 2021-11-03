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

    Seq(name1, name2, name3).foreach { name =>
      val result = name match {
        case Name("Jean", lastName) => s"Jambon $lastName"
        case n @ Name("Guy", _) => s"Guignol ($n)"
        case _ => s"Another person"
      }
      println(result)
    }

  }

}
