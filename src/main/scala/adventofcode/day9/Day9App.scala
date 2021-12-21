package org.example.testsscala
package adventofcode.day9

import scala.io.Source
import scala.util.Using

object Day9App {

  def main(args: Array[String]): Unit = {
    val grid = readInputLines { linesIterator =>
      val lines = linesIterator
        .map { line =>
          line.split("").filter(_.nonEmpty).map(_.toInt).toIndexedSeq
        }
        .toIndexedSeq
      Grid(lines)
    }

    val updatedGrid = grid.markLowPoints()
    updatedGrid.printToTerminal()

    println()

    println(s"Total risk level: ${updatedGrid.totalRiskLevel}")
  }

  private def readInputLines[T](f: Iterator[String] => T): T = {
    Using(Source.fromURL(getClass.getResource("input.txt"))) { input =>
      f(input.getLines)
    }.get
  }

  case class Position(x: Int, y: Int)

  class Grid(private val lines: IndexedSeq[IndexedSeq[Cell]]) {

    def totalRiskLevel: Int = lines.flatten.map(_.riskLevel).sum

    def markLowPoints(): Grid = {
      lines.indices
        .flatMap { y =>
          lines(0).indices.map(x => Position(x, y))
        }
        .foldLeft(this) {
          case (currentGrid, position) =>
            currentGrid.markLowPoint(position)
        }
    }

    private def markLowPoint(position: Position): Grid = {
      val lowestAdjacentCellValue = adjacentCells(position).map(_.height).min
      val cellValue = cellAt(position).height
      val lowest = cellValue < lowestAdjacentCellValue
      updateCellAt(position) { cell => cell.defineLow(lowest) }
    }

    private def updateCellAt(position: Position)(f: Cell => Cell): Grid = {
      val cell = cellAt(position)
      val newCell = f(cell)
      val newLine = lines(position.y).updated(position.x, newCell)
      val newLines = lines.updated(position.y, newLine)
      new Grid(newLines)
    }

    private def cellAt(position: Position): Cell = lines(position.y)(position.x)

    private def adjacentCells(position: Position): Seq[Cell] = {
      val xRange = lines(0).indices
      val yRange = lines.indices

      val possiblePositions = Seq(
        Position(position.x - 1, position.y),
        Position(position.x + 1, position.y),
        Position(position.x, position.y - 1),
        Position(position.x, position.y + 1)
      )

      possiblePositions
        .filter(position => xRange.contains(position.x) && yRange.contains(position.y))
        .map(cellAt)
    }

    def printToTerminal(): Unit = {
      lines.foreach { line =>
        val displayableLine = line.map(cellToTerminal).mkString
        println(displayableLine)
      }
    }

    private def cellToTerminal(cell: Cell): String = {
      if (cell.lowest.getOrElse(false)) {
        displayPurple(cell.height.toString)
      } else {
        cell.height.toString
      }
    }

    private def displayPurple(s: String): String = s"\u001B[35m${s}\u001B[0m"

  }

  object Grid {

    def apply(lines: IndexedSeq[IndexedSeq[Int]]): Grid = {
      val cellLines = lines.map { line =>
        line.map(Cell(_))
      }
      new Grid(cellLines)
    }

  }

  case class Cell(height: Int, lowest: Option[Boolean] = None) {

    def defineLow(lowest: Boolean): Cell = copy(lowest = Some(lowest))

    def riskLevel: Int = {
      lowest match {
        case Some(true) =>
          height + 1
        case _ => 0
      }
    }

  }

}
