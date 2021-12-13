package org.example.testsscala
package adventofcode.day6

case class Lanternfish(daysUntilReproduction: Int) {

  def nextDay(): (Lanternfish, Option[Lanternfish]) = {
    if (daysUntilReproduction == 0) {
      (Lanternfish(6), Some(Lanternfish(8)))
    } else {
      (Lanternfish(daysUntilReproduction - 1), None)
    }
  }

}
