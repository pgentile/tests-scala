package org.example.testsscala
package adventofcode.day4

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

object Day4App {

  case class Cell(value: Int, marked: Boolean = false) {

    def mark(): Cell = copy(marked = true)

    override def toString: String = {
      val valueRepr = f"$value%02d"
      val markedRepr = if (marked) "*" else " "
      valueRepr + markedRepr
    }
  }

  case class Coordinates(x: Int, y: Int)

  case class Grid private(private val lines: Seq[Seq[Cell]]) {

    private val coordinatesByValue: Map[Int, Coordinates] = {
      val entries = for {
        y <- lines.indices
        line = lines(y)
        x <- line.indices
        value = line(x)
      } yield value.value -> Coordinates(x, y)

      Map.from(entries)
    }

    def play(value: Int): Either[Grid, Grid] = {
      mark(value)
        .map {
          case (newGrid, coordinates) =>
            val cellsOfMarkedColumn = newGrid.cellsOfColumn(coordinates.x)
            val columnMarked = cellsOfMarkedColumn.forall(_.marked)

            val cellsOfMarkedLine = newGrid.cellsOfLine(coordinates.y)
            val lineMarked = cellsOfMarkedLine.forall(_.marked)

            val success = columnMarked || lineMarked
            Either.cond(success, newGrid, newGrid)
        }
        .getOrElse(Left(this))
    }

    def sumOfUnmarkedValues: Int = {
      lines.flatten
        .filter(cell => !cell.marked)
        .map(_.value)
        .sum
    }

    private def cellsOfLine(y: Int): Seq[Cell] = lines(y)

    private def cellsOfColumn(x: Int): Seq[Cell] = lines.map(line => line(x))

    private def mark(value: Int): Option[(Grid, Coordinates)] = {
      coordinatesByValue.get(value).map { coordinates =>
        val Coordinates(x, y) = coordinates
        val line = lines(y)
        val cell = line(x)
        assert(cell.value == value)
        val updatedLines = lines.updated(y, line.updated(x, cell.mark()))
        (copy(lines = updatedLines), coordinates)
      }
    }

    override def toString: String = {
      lines.map { cells =>
        cells.map(_.toString).mkString(" ")
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
    play(grids, pickedNumbers) match {
      case Left(_) => println("No solution found")
      case Right((grid, score)) =>
        println(grid)
        println()
        println(s"Score is: $score")
    }
  }

  def play(grids: Seq[Grid], pickedNumbers: Seq[Int]): Either[Unit, (Grid, Int)] = {
    pickedNumbers match {
      // Play with first picked number
      case pickedNumber +: remaining =>
        val playedGrids = grids.map(grid => grid.play(pickedNumber))
        playedGrids
          .collectFirst[Either[Unit, (Grid, Int)]] {
            // This is the winning grid!
            case Right(updatedGrid) =>
              val score = pickedNumber * updatedGrid.sumOfUnmarkedValues
              Right((updatedGrid, score))
          }
          .getOrElse {
            // No winning grid? Play remaining picked numbers
            val nextGrids = playedGrids.map(updatedGrid => updatedGrid.fold(identity, identity))
            play(nextGrids, remaining)
          }

      // No more picked numbers
      case Nil =>
        Left()
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
