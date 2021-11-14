package org.example.testsscala

import scala.language.implicitConversions

package object complex {

  val i: Complex = Complex(0, 1)

  implicit class DoubleExtensions(number: Double) {

    def i: Complex = Complex(0, number)

  }

  implicit def double2complex(number: Double): Complex = Complex(number, 0)

}
