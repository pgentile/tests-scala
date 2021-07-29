package org.example.testsscala
package hello

object Hello {

  def main(args: Array[String]): Unit = {
    val name = args.headOption.getOrElse("inconnu")
    println(s"Bonjour, $name")
  }

}
