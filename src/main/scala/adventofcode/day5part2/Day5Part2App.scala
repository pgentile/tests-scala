package org.example.testsscala
package adventofcode.day5part2

import scala.collection.AbstractIterator
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using
import cats.implicits._

object Day5Part2App {

  case class Point(x: Int, y: Int)

  sealed trait Line {

    def from: Point

    def to: Point

    def points: Iterator[Point]

  }

  case class HorizontalLine(private val x: Range, private val y: Int) extends Line {

    override def from: Point = Point(x.start, y)

    override def to: Point = Point(x.last, y)

    override def points: Iterator[Point] = x.iterator.map(Point(_, y))

  }

  case class VerticalLine(private val x: Int, private val y: Range) extends Line {

    override def from: Point = Point(x, y.start)

    override def to: Point = Point(x, y.last)

    override def points: Iterator[Point] = y.iterator.map(Point(x, _))

  }

  case class DiagonalLine(override val from: Point, override val to: Point) extends Line {
    self =>

    override def points: Iterator[Point] = new AbstractIterator[Point] {

      private val xIncrement = if (self.from.x <= self.to.x) 1 else -1
      private val yIncrement = if (self.from.y <= self.to.y) 1 else -1
      private val maxCount = Math.abs(self.from.x - self.to.x)

      private var count = 0
      private var x = from.x
      private var y = from.y

      override def hasNext: Boolean = {
        count <= maxCount
      }

      override def next(): Point = {
        val point = Point(x, y)
        x += xIncrement
        y += yIncrement
        count += 1
        point
      }

    }

  }

  def main(args: Array[String]): Unit = {
    val lines = parseLines()
    val grid = initGrid(lines)

    lines.foreach { line =>
      // println(line)
      line.points.foreach { point =>
        grid(point.y)(point.x) += 1
      }

      // println()
      // printGrid(grid)
      // println()
    }

    val overlaps = grid.flatten.count(_ >= 2)
    println(s"Overlaps = $overlaps")
  }

  type Grid = ArrayBuffer[ArrayBuffer[Int]]

  private def initGrid(lines: Seq[Line]): Grid = {
    val xMax = lines.map(_.to.x).max
    val yMax = lines.map(_.to.y).max

    ArrayBuffer.fill(yMax + 1) {
      ArrayBuffer.fill(xMax + 1)(0)
    }
  }

  private def printGrid(grid: Grid): Unit = {
    val repr = grid.map { line =>
      line.map { number =>
        if (number > 0) f"$number%01d" else "."
      }.mkString
    }.mkString("\n")
    println(repr)
  }

  private def parseLines(): Seq[Line] = {
    readInputLines { sourceLines =>
      sourceLines
        .map(_.split("->").map(_.trim))
        .map {
          case Array(first, last) =>
            parsePoint(first) -> parsePoint(last)
        }
        .map(select {
          case (first, last) if first.x == last.x =>
            VerticalLine(first.x, newRange(first.y, last.y))
          case (first, last) if first.y == last.y =>
            HorizontalLine(newRange(first.x, last.x), first.y)
          case (first, last) if isDiagonal(first, last) =>
            DiagonalLine(first, last)
        })
        // .tapEach { parsedLine =>
        //   println(parsedLine)
        // }
        .collect {
          case Right(line) => line
        }
        .toSeq
    }
  }

  private def select[A, B](f: PartialFunction[A, B]): A => Either[A, B] = {
    val fWrapped = f.andThen(Right(_))
    (input: A) => fWrapped.applyOrElse(input, Left[A, B])
  }

  private def isDiagonal(a: Point, b: Point): Boolean = {
    Math.abs(a.x - b.x) == Math.abs(a.y - b.y)
  }

  private def newRange(a: Int, b: Int): Range = Math.min(a, b) to Math.max(a, b)

  private def parsePoint(source: String): Point = {
    source.split(",") match {
      case Array(first, last) => Point(first.toInt, last.toInt)
    }
  }

  private def readInputLines[T](f: Iterator[String] => T): T = {
    Using(Source.fromURL(getClass.getResource("input.txt"))) { input =>
      f(input.getLines)
    }.get
  }

}
