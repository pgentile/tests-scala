package org.example.testsscala
package testyield

import scala.language.implicitConversions

object Yield5Main {

  def main(args: Array[String]): Unit = {
    val value = for (w1 <- new Wrapper("Pierre"); w2 <- new Wrapper("Julie")) yield s"$w1 / $w2"
    println(value)
  }

  class Wrapper[+A](val value: A) {

    def map[B](f: A => B): Wrapper[B] = new Wrapper(f(value))

    def flatMap[B](f: A => Wrapper[B]): Wrapper[B] = f(value)

    override def toString: String = s"Wrapper($value)"

  }

}
