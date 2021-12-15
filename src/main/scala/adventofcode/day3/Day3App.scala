package org.example.testsscala
package adventofcode.day3

import scala.io.Source
import scala.util.Using

object Day3App {

  def main(args: Array[String]): Unit = {
    Using(Source.fromURL(Day3App.getClass.getResource("input.txt"))) { input =>
     val numbers = input.getLines
       .map(Integer.parseInt(_, 2))
       .toSeq

      val (zeros, ones) = numbers
        .map(n => (n >>> 11) & 0x1) // Read each bit
        .partition(n => n == 0)

      val countZeros = zeros.length
      val countOnes = ones.length

      val result = if (countZeros > countOnes) 0 else 1
      println(result)
    }.get
  }

}
