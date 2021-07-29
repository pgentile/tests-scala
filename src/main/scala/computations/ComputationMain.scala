package org.example.testsscala
package computations

object ComputationMain {

  def main(args: Array[String]): Unit = {
    val c1 = Computation(println("Coucou"))

    val computation = Computation {
      "Pierre"
    }.map(name => {
      println(s"Hello, $name")
    })

    computation.execute()
  }

}
