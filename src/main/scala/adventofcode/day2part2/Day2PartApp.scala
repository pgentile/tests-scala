package org.example.testsscala
package adventofcode.day2part2

import scala.io.Source
import scala.util.Using

object Day2PartApp {

  case class SubmarineState(x: Int = 0, y: Int = 0, aim: Int = 0) {

    def forward(value: Int): SubmarineState = copy(x = x + value, y = y + (aim * value))
    def down(value: Int): SubmarineState = copy(aim = aim + value)
    def up(value: Int): SubmarineState = copy(aim = aim - value)

  }

  def main(args: Array[String]): Unit = {
    Using(Source.fromURL(Day2PartApp.getClass.getResource("input.txt"))) { input =>
      val newState = input.getLines
        .filter(_.nonEmpty)
        .map(_.split(' ').toList)
        .collect { case action :: countStr :: Nil => (action, countStr.toInt) }
        .foldLeft(SubmarineState()) { (state, move) =>
          val (action, value) = move
          val newState = action match {
            case "forward" => state.forward(value)
            case "down" => state.down(value)
            case "up" => state.up(value)
            case _ => throw new IllegalStateException("Invalid action")
          }
          newState
        }
      println(s"Result = ${newState.x * newState.y}")
    }.get
  }

}
