package org.example.testsscala
package enums

import enumeratum.{Enum, EnumEntry}

sealed trait CardinalPoint extends EnumEntry {

  def direction: Int

}

object CardinalPoint extends Enum[CardinalPoint] {

  override val values: IndexedSeq[CardinalPoint] = findValues

  case object North extends CardinalPoint {
    override val direction: Int = 0
  }

  case object East extends CardinalPoint {
    override val direction: Int = 90
  }

  case object South extends CardinalPoint {
    override val direction: Int = 180
  }

  case object West extends CardinalPoint {
    override val direction: Int = 270
  }

}
