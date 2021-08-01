package org.example.testsscala
package random

object PlayWithTypesApp extends App {

  class Toto[A, B]
  class Titi[A]

  type TotoTyped1 = Toto[Int, String]
  type TotoTyped2 = Int Toto String

}
