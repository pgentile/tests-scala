package org.example.testsscala
package adventofcode.day9part2

import scala.io.Source
import scala.util.Using

object Day9Part2App {

  def main(args: Array[String]): Unit = {
    val grid = readInputLines { linesIterator =>
      val lines = linesIterator
        .map { line =>
          line.split("").filter(_.nonEmpty).map(_.toInt).toIndexedSeq
        }
        .toIndexedSeq
      Grid(lines)
    }

    grid.execute().printToTerminal()
  }

  private def readInputLines[T](f: Iterator[String] => T): T = {
    Using(Source.fromURL(getClass.getResource("input.txt"))) { input =>
      f(input.getLines)
    }.get
  }

  case class Position(x: Int, y: Int)

  case class CellGroup(private val cells: Seq[Cell]) {

    def addCell(cell: Cell): CellGroup = CellGroup(cells :+ cell)

    def size: Int = cells.length

  }

  class Grid(private val lines: IndexedSeq[IndexedSeq[Cell]], private val groups: Seq[CellGroup] = Nil) {

    def execute(): Grid = markLowPoints().markBasins()

    private def newGroup(cell: Cell): (Grid, CellGroup) = {
      val group = CellGroup(Seq(cell))
      val newGrid = new Grid(lines, groups :+ group).updateCellAt(cell.position) { cell =>
        cell.setGroup(group)
      }
      (newGrid, group)
    }

    private def markLowPoints(): Grid = {
      lines.indices
        .flatMap { y =>
          lines(0).indices.map(x => Position(x, y))
        }
        .foldLeft(this) {
          case (currentGrid, position) => currentGrid.markLowPoint(position)
        }
    }

    private def markBasins(): Grid = {
      lines.flatten
        .filter(_.category == Basin)
        .foldLeft(this) {
          case (currentGrid, cell) => currentGrid.markBassinNear(cell.position)
        }
    }

    private def markLowPoint(position: Position): Grid = {
      val lowestAdjacentCellValue = adjacentCells(position).map(_.height).min
      val height = cellAt(position).height
      val lowest = height < lowestAdjacentCellValue
      if (lowest) {
        updateCellAt(position) { cell => cell.defineBasin() }
      } else {
        this
      }
    }

    private def markBassinNear(position: Position): Grid = {
      val height = cellAt(position).height
      val allowedHeights = height until 9
      adjacentCells(position)
        .filter(cell => allowedHeights.contains(cell.height))
        .filter(cell => cell.category == UndefinedCategory)
        .foldLeft(this) {
          case (currentGrid, cell) =>
            currentGrid.markBasin(cell.position).markBassinNear(cell.position)
        }
    }

    private def markBasin(position: Position): Grid = {
      updateCellAt(position) { cell => cell.defineBasin() }
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
      cell.category match {
        case Basin => displayBlue(cell.height.toString)
        case _ => cell.height.toString
      }
    }

    private def displayBlue(s: String): String = s"\u001B[36m${s}\u001B[0m"

  }

  object Grid {

    def apply(lines: IndexedSeq[IndexedSeq[Int]]): Grid = {
      val cellLines = lines.indices.map { y =>
        val line = lines(y)
        line.indices.map { x =>
          val height = line(x)
          val position = Position(x, y)
          Cell(position = position, height = height)
        }
      }
      new Grid(cellLines)
    }

  }

  sealed trait CellCategory

  object UndefinedCategory extends CellCategory

  object Basin extends CellCategory

  case class Cell(position: Position, height: Int, category: CellCategory = UndefinedCategory, group: Option[CellGroup] = None) {

    def setGroup(group: CellGroup): Cell = copy(group = Some(group))

    def defineBasin(): Cell = copy(category = Basin)

  }

}
