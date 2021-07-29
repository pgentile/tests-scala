package org.example.testsscala
package hello

object HelloManyTimes {

  def main(args: Array[String]): Unit = {
    val name = args.headOption.getOrElse("inconnu")
    (1 to 5).foreach(n => println(s"Bonjour, $name pour la ${n}e fois"))
  }

}
