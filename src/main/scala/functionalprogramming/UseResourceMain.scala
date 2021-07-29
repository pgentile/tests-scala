package org.example.testsscala
package functionalprogramming

import scala.collection.mutable
import scala.util.{Random, Try}

object UseResourceMain {

  def main(args: Array[String]): Unit = {
    println("Coucou")

    use(AutoClose) {
      println(s"Dans la face: ${Random.nextInt()}")
    }

    manage { use =>
      println("Use many")
      use(AutoClose)
      use(AutoClose)
    }
  }

  object AutoClose extends AutoCloseable {

    override def close(): Unit = println("Closing")

  }

  def use[A](resource: AutoCloseable)(f: => A): A = try f finally resource.close()

  def manage[A](f: (AutoCloseable => Unit) => A): A = {
    val resources = mutable.Queue[AutoCloseable]()

    val use: AutoCloseable => Unit = { resource: AutoCloseable =>
      resources.enqueue(resource)
    }

    val result = f(use)

    for (resource <- resources.reverse) {
      Try(resource.close())
    }

    result
  }

}
