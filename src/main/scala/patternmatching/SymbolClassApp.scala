package org.example.testsscala
package patternmatching

import patternmatching.SymbolClassApp.<===>

object SymbolClassApp extends App {

  class <===>(val left: Int, val right: Int) {

  }

  object <===> {

    def apply(left: Int, right: Int) = new <===>(left, right)

    def unapply(instance: <===>): Option[(Int, Int)] = Some((instance.left, instance.right))

  }

  val o = <===>(1, 2)
  val result = o match {
    case left <===> right => Some(left == right)
    case <===>(1, 2) => Some(true)
    case _ => None
  }
  println(result)

}
