package org.example.testsscala
package lambdas

object LambdaApp {

  def main(args: Array[String]): Unit = {
    println("Should call some lambda")
    val x = -1
    List(1, 2, 3).flatMap { i =>
      List(i, i, x)
    }
  }

}
