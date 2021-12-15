package org.example.testsscala
package adventofcode.day3

import scala.io.Source
import scala.util.Using

object Day3App {

  def main(args: Array[String]): Unit = {
    val entrySize = readInputLines(_.head.length)

    readInputLines { lines =>
      val mostCommonValues: String = (0 until entrySize)
        .map { index =>
          val (zeros, ones) = lines
            .map(line => line.charAt(index)) // Read each bit
            .partition(n => n == '0')

          if (zeros.length > ones.length) '0' else '1'
        }
        .mkString

      // Reverse from most common values
      val leastCommonValues = mostCommonValues.map { c =>
        if (c == '1') '0' else '1'
      }

      val gamma = Integer.parseInt(mostCommonValues, 2)
      val epsilon = Integer.parseInt(leastCommonValues, 2)

      println(mostCommonValues)
      println(leastCommonValues)
      // Valid result: 3958484
      println(gamma * epsilon)
    }
  }

  private def readInputLines[T](f: (Seq[String] => T)): T = {
    Using(Source.fromURL(Day3App.getClass.getResource("input.txt"))) { input =>
      f(input.getLines.toSeq)
    }.get
  }

}
