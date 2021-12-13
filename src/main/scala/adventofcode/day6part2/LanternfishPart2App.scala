package org.example.testsscala
package adventofcode.day6part2

object LanternfishPart2App {

  def main(args: Array[String]): Unit = {
    val input = "1,4,2,4,5,3,5,2,2,5,2,1,2,4,5,2,3,5,4,3,3,1,2,3,2,1,4,4,2,1,1,4,1,4,4,4,1,4,2,4,3,3,3,3,1,1,5,4,2,5,2,4,2,2,3,1,2,5,2,4,1,5,3,5,1,4,5,3,1,4,5,2,4,5,3,1,2,5,1,2,2,1,5,5,1,1,1,4,2,5,4,3,3,1,3,4,1,1,2,2,2,5,4,4,3,2,1,1,1,1,2,5,1,3,2,1,4,4,2,1,4,5,2,5,5,3,3,1,3,2,2,3,4,1,3,1,5,4,2,5,2,4,1,5,1,4,5,1,2,4,4,1,4,1,4,4,2,2,5,4,1,3,1,3,3,1,5,1,5,5,5,1,3,1,2,1,4,5,4,4,1,3,3,1,4,1,2,1,3,2,1,5,5,3,3,1,3,5,1,5,3,5,3,1,1,1,1,4,4,3,5,5,1,1,2,2,5,5,3,2,5,2,3,4,4,1,1,2,2,4,3,5,5,1,1,5,4,3,1,3,1,2,4,4,4,4,1,4,3,4,1,3,5,5,5,1,3,5,4,3,1,3,5,4,4,3,4,2,1,1,3,1,1,2,4,1,4,1,1,1,5,5,1,3,4,1,1,5,4,4,2,2,1,3,4,4,2,2,2,3"

    // Key: age of the lanternfish. Value: number of lanterfish
    val initialLanternfishes = input.split(",").map(_.toInt).groupMapReduce(identity)(_ => 1L)(_ + _)
    println(initialLanternfishes)

    val result = (1 to 256).foldLeft(initialLanternfishes) { (fishes, _) =>
      reproduceFishes(fishes)
    }

    val count = result.values.sum
    println(count)
  }

  def reproduceFishes(fishes: Map[Int, Long]): Map[Int, Long] = {
    fishes.iterator
      .flatMap { entry =>
        val (age, count) = entry
        reproduceOneFish(age).toSeq.map { newAge =>
          newAge -> count
        }
      }
      .toSeq
      .groupMapReduce(_._1)(_._2)(_ + _)
  }

  def reproduceOneFish(age: Int): Set[Int] = {
    if (age == 0) {
      Set(6, 8)
    } else {
      Set(age - 1)
    }
  }


}
