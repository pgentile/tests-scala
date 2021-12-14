package org.example.testsscala
package adventofcode.day2

import scala.io.Source
import scala.util.Using

object Day2App {

  case class Coordinates(x: Int = 0, y: Int = 0)

  def main(args: Array[String]): Unit = {
    Using(Source.fromURL(Day2App.getClass.getResource("input.txt"))) { input =>
      val newCoordinates = input.getLines
        .filter(_.nonEmpty)
        .map(_.split(' ').toList)
        .collect { case action :: countStr :: Nil => (action, countStr.toInt) }
        .foldLeft(Coordinates()) { (coordinates, move) =>
          val (action, value) = move
          action match {
            case "forward" => coordinates.copy(x = coordinates.x + value)
            case "down" => coordinates.copy(y = coordinates.y + value)
            case "up" => coordinates.copy(y = coordinates.y - value)
            case _ => throw new IllegalStateException("Invalid action")
          }
        }
      println(s"Result = ${newCoordinates.x * newCoordinates.y}")
    }.get
  }

}
