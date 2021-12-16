package org.example.testsscala
package adventofcode.day4

import adventofcode.day3part2.Day3Part2App

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

object Day4App {

  case class Cell(value: Int, check: Boolean = false)

  case class Grid(lines: Seq[Seq[Cell]]) {

    override def toString: String = {
      lines.map { cells =>
        cells.map(cell => f"${cell.value}%02d").mkString(" ")
      }.mkString("\n")
    }

  }

  object Grid {

    def fromValues(lines: Seq[Int]*): Grid = {
      Grid(lines.map { values =>
        values.map(Cell(_))
      })
    }
  }

  def main(args: Array[String]): Unit = {
   val (pickedNumbers, grids) = parseInput()
    grids.foreach { grid =>
      println(grid)
      println()
    }
  }

  def parseInput(): (Seq[Int], Seq[Grid]) = {
    readInputLines { lines =>
      val pickedNumbers = parsePickedNumbers(lines)

      val grids = ArrayBuffer.empty[Grid]
      while (lines.hasNext) {
        skipLine(lines)

        val grid = parseGrid(lines)
        grids += grid
      }

      (pickedNumbers, grids.toSeq)
    }
  }

  private def parsePickedNumbers(lines: Iterator[String]): Seq[Int] = {
    lines.next().split(',').map(_.toInt).toSeq
  }

  private def parseGrid(lines: Iterator[String]): Grid = {
    val allLines = ArrayBuffer.empty[Seq[Int]]

    (1 to 5).map { _ =>
      val line = lines.next().trim.split(" +").map(_.toInt).toSeq
      allLines += line
    }

    Grid.fromValues(allLines.toSeq: _*)
  }

  private def skipLine(lines: Iterator[String]): Unit = lines.next()

  private def readInputLines[T](f: Iterator[String] => T): T = {
    Using(Source.fromURL(getClass.getResource("input.txt"))) { input =>
      f(input.getLines)
    }.get
  }

}
