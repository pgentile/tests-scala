package org.example.testsscala
package catshelpers

import cats.syntax.all._

object CatsToFutureApp {

  def main(args: Array[String]): Unit = {
    println("123".some)
    println("123".some)
    println("123".asRight[Exception])

    println("ABC".asFuture)
    println(new RuntimeException().asFuture)
  }

}
