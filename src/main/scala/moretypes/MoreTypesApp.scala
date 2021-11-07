package org.example.testsscala
package moretypes

import scala.language.reflectiveCalls

object MoreTypesApp extends App {

  // Duck typing with Scala

  type HelloLike = {
    def hello(name: String): Unit
    def goodbye(): Unit
  }

  class BonjourService {
    def hello(name: String): Unit = println(s"Bonjour, $name")
    def goodbye(): Unit = println("Au revoir")
  }

  object HelloService {
    def hello(name: String): Unit = println(s"Hello, $name")
    def goodbye(): Unit = println("Bye bye")
  }

  def greet(helloLike: HelloLike): Unit = {
    helloLike.hello("Pierre")
    helloLike.goodbye()
  }

  greet(new BonjourService)
  greet(HelloService)

}