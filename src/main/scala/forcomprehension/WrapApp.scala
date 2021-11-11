package org.example.testsscala
package forcomprehension

object WrapApp extends App {

  val example: Option[Int] = for {
    _ <- Some()
    _ <- Some()
    x = throw new IllegalStateException
  } yield x

  println(example)

}
