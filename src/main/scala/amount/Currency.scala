package org.example.testsscala
package amount

sealed trait Currency {
  val symbol: String
}

case object EUR extends Currency {
  override val symbol = "€"
}

case object USD extends Currency {
  override val symbol: String = "$"
}
