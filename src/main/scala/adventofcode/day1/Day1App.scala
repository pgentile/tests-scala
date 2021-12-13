package org.example.testsscala
package adventofcode.day1

import scala.io.Source
import scala.util.Using


object Day1App {

  def main(args: Array[String]): Unit = {
    val resource = classOf[Day1App.type].getResource("input.txt")
    Using(Source.fromURL(resource)) { source =>
      val lines = source.getLines
        .filter(_.nonEmpty)
        .map(_.toInt)
        .toSeq

      val count = lines
        .sliding(2)
        .drop(1)
        .map {
          case before :: after :: Nil =>
            if (after > before) {
              1
            } else {
              0
            }
        }.sum

      println(s"Count = $count")
    }

  }

}
