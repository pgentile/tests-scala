package org.example.testsscala
package adventofcode.day1part2

import scala.io.Source
import scala.util.Using


object Day1Part2App {

  def main(args: Array[String]): Unit = {
    val resource = classOf[Day1Part2App.type].getResource("input.txt")
    Using(Source.fromURL(resource)) { source =>
      val lines = source.getLines
        .filter(_.nonEmpty)
        .map(_.toInt)
        .toSeq

      val incrementCount = lines
        .sliding(3)
        .map(_.sum)
        .toSeq
        .sliding(2)
        .map {
          case before :: after :: Nil =>
            if (after > before) {
              1
            } else {
              0
            }
        }
        .sum

      println(incrementCount)

    }.get
  }

}
