package org.example.testsscala
package adventofcode.day7

import scala.io.Source
import scala.util.Using

object Day7App {

  def main(args: Array[String]): Unit = {
    val crabPositions = readCrabPositions()
    val (testPosition, fuelSpent) = minimumFuelSpent(crabPositions)
    println(s"Fuel spent for position $testPosition: $fuelSpent")
  }

  private def minimumFuelSpent(positions: Seq[Int]): (Int, Int) = {
    fuelSpedsForAllPositions(positions).minBy(_._2)
  }

  private def fuelSpedsForAllPositions(positions: Seq[Int]): Seq[(Int, Int)] = {
    positionRange(positions)
      .map { testPosition =>
        testPosition -> fuelSpentForPosition(positions, testPosition)
      }
  }

  private def fuelSpentForPosition(positions: Seq[Int], testPosition: Int) = {
    positions.map(pos => Math.abs(pos - testPosition)).sum
  }

  private def readCrabPositions(): Seq[Int] = {
    readInputLines(_.next()).split(',').map(_.toInt)
  }

  def positionRange(positions: Seq[Int]): Range = positions.min to positions.max

  private def readInputLines[T](f: Iterator[String] => T): T = {
    Using(Source.fromURL(getClass.getResource("input.txt"))) { input =>
      f(input.getLines)
    }.get
  }

}
