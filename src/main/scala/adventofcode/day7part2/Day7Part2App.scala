package org.example.testsscala
package adventofcode.day7part2

import scala.io.Source
import scala.util.Using

object Day7Part2App {

  def main(args: Array[String]): Unit = {
    val crabPositions = readCrabPositions()
    val (testPosition, fuelSpent) = minimumFuelSpent(crabPositions)
    println(s"Fuel spent for position $testPosition: $fuelSpent")
  }

  private def minimumFuelSpent(positions: Seq[Int]): (Int, Int) = {
    fuelSpentForAllPositions(positions).minBy(_._2)
  }

  private def fuelSpentForAllPositions(positions: Seq[Int]): Seq[(Int, Int)] = {
    positionRange(positions)
      .map { testPosition =>
        testPosition -> fuelSpentForPosition(positions, testPosition)
      }
  }

  private def fuelSpentForPosition(positions: Seq[Int], testPosition: Int) = {
    positions.map(pos => fuelSpentForMove(Math.abs(pos - testPosition))).sum
  }

  /**
   * U(0) = 0
   * U(n+1) = U(n) + 1
   *
   * Sum(U(n)) = 0 + 1 + 2 + ... + (n - 2) + (n - 1) + n
   *
   * See https://fr.wikipedia.org/wiki/Suite_arithm%C3%A9tique#Somme_des_termes
   */
  private def fuelSpentForMove(move: Int): Int = (move * (move + 1)) / 2

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
