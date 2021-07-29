package org.example.testsscala
package amount

sealed abstract case class Currency(symbol: String)
object EUR extends Currency("€")
object USD extends Currency("$")
