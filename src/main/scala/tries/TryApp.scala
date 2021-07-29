package org.example.testsscala
package tries

import scala.util.{Failure, Success}

object TryApp extends App {

  val t1 = Success(1)
  val t2 = Failure[Int](new RuntimeException("Echec 2"))
  val t3 = Success(3)
  val t4 = Failure[Int](new RuntimeException("Echec 4")).filter(_ % 2 == 1)

  val result = for (v1 <- t1; v2 <- t2; v3 <- t3; v4 <- t4) yield v1 + v2 + v3 + v4
  println(result)

}
