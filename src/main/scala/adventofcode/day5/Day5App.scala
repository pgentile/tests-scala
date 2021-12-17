package org.example.testsscala
package adventofcode.day5

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

object Day5App {

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

  def main(args: Array[String]): Unit = {
    val lines = parseLines()
    val grid = initGrid(lines)

    lines.flatMap(_.points).foreach { point =>
      grid(point.y)(point.x) += 1
    }

    val overlaps = grid.flatten.count(_ >= 2)
    println(s"Overlaps = $overlaps")
  }

  private def initGrid(lines: Seq[Line]): ArrayBuffer[ArrayBuffer[Int]] = {
    val xMax = lines.map(_.to.x).max
    val yMax = lines.map(_.to.y).max

    ArrayBuffer.fill(yMax + 1) {
      ArrayBuffer.fill(xMax + 1)(0)
    }
  }

  private def parseLines(): Seq[Line] = {
    readInputLines { sourceLines =>
      sourceLines
        .map(_.split("->").map(_.trim))
        .map {
          case Array(first, last) =>
            parsePoint(first) -> parsePoint(last)
        }
        .collect {
          case (first, last) if first.x == last.x => VerticalLine(first.x, newRange(first.y, last.y))
          case (first, last) if first.y == last.y => HorizontalLine(newRange(first.x, last.x), first.y)
        }
        .toSeq
    }
  }

  private def newRange(a: Int, b: Int): Range = {
    if (a >= b) {
      b to a
    } else {
      a to b
    }
  }

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
