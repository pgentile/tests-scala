package org.example.testsscala
package adventofcode.day3part2

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Using

object Day3Part2App {

  def main(args: Array[String]): Unit = {
    readInputLines { lines =>
      val oxygen = Integer.parseInt(findLineMatchingMostCommon(lines), 2)
      val co2 = Integer.parseInt(findLineMatchingLeastCommon(lines), 2)
      println(oxygen)
      println(co2)
      println(oxygen * co2)
    }
  }

  private def readInputLines[T](f: Seq[String] => T): T = {
    Using(Source.fromURL(Day3Part2App.getClass.getResource("input.txt"))) { input =>
      f(input.getLines.toSeq)
    }.get
  }

  @tailrec
  private def findLineMatchingMostCommon(lines: Seq[String], index: Int = 0): String = {
    val (zeroAtIndex, oneAtIndex) = lines.partition(line => line.charAt(index) == '0')
    val selectedLines = if (oneAtIndex.length >= zeroAtIndex.length) oneAtIndex else zeroAtIndex
    if (selectedLines.length == 1) {
      selectedLines.head
    } else {
      findLineMatchingMostCommon(selectedLines, index + 1)
    }
  }

  @tailrec
  private def findLineMatchingLeastCommon(lines: Seq[String], index: Int = 0): String = {
    val (zeroAtIndex, oneAtIndex) = lines.partition(line => line.charAt(index) == '0')
    val selectedLines = if (zeroAtIndex.length <= oneAtIndex.length) zeroAtIndex else oneAtIndex
    if (selectedLines.length == 1) {
      selectedLines.head
    } else {
      findLineMatchingLeastCommon(selectedLines, index + 1)
    }
  }
}
