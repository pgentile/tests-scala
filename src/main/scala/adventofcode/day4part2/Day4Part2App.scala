package org.example.testsscala
package adventofcode.day4part2

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

object Day4Part2App {

  case class Cell(value: Int, marked: Boolean = false) {

    def mark(): Cell = copy(marked = true)

    override def toString: String = {
      val valueRepr = f"$value%02d"
      val markedRepr = if (marked) "*" else " "
      valueRepr + markedRepr
    }
  }

  case class Coordinates(x: Int, y: Int)

  case class Grid private(
                           private val lines: Seq[Seq[Cell]],
                           private val coordinatesByValue: Map[Int, Coordinates],
                           success: Boolean = false
                         ) {

    def play(value: Int): Grid = {
      if (success) {
        return this
      }

      coordinatesForValue(value)
        .map { coordinates =>
          val markedGrid = mark(coordinates)
          val Coordinates(x, y) = coordinates
          val newSuccess = markedGrid.columnMarked(x) || markedGrid.lineMarked(y)
          markedGrid.copy(success = newSuccess)
        }
        .getOrElse(this)
    }

    def sumOfUnmarkedValues: Int = {
      lines.flatten
        .filter(cell => !cell.marked)
        .map(_.value)
        .sum
    }

    private def mark(coordinates: Coordinates): Grid = {
      val Coordinates(x, y) = coordinates
      val line = lines(y)
      val cell = line(x)
      val updatedLines = lines.updated(y, line.updated(x, cell.mark()))
      copy(lines = updatedLines)
    }

    private def coordinatesForValue(value: Int): Option[Coordinates] = {
      coordinatesByValue.get(value)
    }

    private def lineMarked(y: Int): Boolean = {
      cellsOfLine(y).forall(_.marked)
    }

    private def columnMarked(x: Int): Boolean = {
      cellsOfColumn(x).forall(_.marked)
    }

    private def cellsOfLine(y: Int): Seq[Cell] = lines(y)

    private def cellsOfColumn(x: Int): Seq[Cell] = lines.map(line => line(x))

    override def toString: String = {
      lines.map { cells =>
        cells.map(_.toString).mkString(" ")
      }.mkString("\n")
    }

  }

  object Grid {

    def fromValues(lines: Seq[Int]*): Grid = {
      val cellLines = lines.map { values =>
        values.map(Cell(_))
      }

      val coordinatesByValue: Map[Int, Coordinates] = {
        val entries = for {
          y <- lines.indices
          line = lines(y)
          x <- line.indices
          value = line(x)
        } yield value -> Coordinates(x, y)

        Map.from(entries)
      }

      Grid(cellLines, coordinatesByValue)
    }
  }

  def main(args: Array[String]): Unit = {
    val (pickedNumbers, grids) = parseInput()
    play(grids, pickedNumbers).foreach {
      case PlayResult(grid, successNumber) =>
        val score = grid.sumOfUnmarkedValues * successNumber
        println(s"Score = $score")
    }
  }

  case class PlayResult(grid: Grid, successNumber: Int)

  @tailrec
  def play(grids: Seq[Grid], pickedNumbers: Seq[Int]): Option[PlayResult] = {
    pickedNumbers match {
      // Play with first picked number
      case pickedNumber +: remaining =>
        val playedGrids = grids.map(grid => grid.play(pickedNumber))

        val allSuccessful = playedGrids.forall(_.success)
        if (allSuccessful) {
          val lastSuccessfulGrid = playedGrids.filter(grid => !grids.contains(grid)).head
          Some(PlayResult(lastSuccessfulGrid, pickedNumber))
        } else {
          play(playedGrids, remaining)
        }

      // No more picked numbers
      case Nil =>
        None
    }
  }

  def parseInput(): (Seq[Int], Seq[Grid]) = {
    readInputLines {
      lines =>
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

    (1 to 5).map {
      _ =>
        val line = lines.next().trim.split(" +").map(_.toInt).toSeq
        allLines += line
    }

    Grid.fromValues(allLines.toSeq: _*)
  }

  private def skipLine(lines: Iterator[String]): Unit = lines.next()

  private def readInputLines[T](f: Iterator[String] => T): T = {
    Using(Source.fromURL(getClass.getResource("input.txt"))) {
      input =>
        f(input.getLines)
    }.get
  }

}
