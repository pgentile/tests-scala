package org.example.testsscala
package either

object EitherMain extends App {

  type SampleEither = Exception Either Int
  type SampleOption = Option[Int]

  val toto: SampleEither = Right(1)
  println(toto)

}

