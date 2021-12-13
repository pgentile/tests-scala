package org.example.testsscala
package adventofcode.day6

object LanternfishApp {

  def main(args: Array[String]): Unit = {
    val input = "1,4,2,4,5,3,5,2,2,5,2,1,2,4,5,2,3,5,4,3,3,1,2,3,2,1,4,4,2,1,1,4,1,4,4,4,1,4,2,4,3,3,3,3,1,1,5,4,2,5,2,4,2,2,3,1,2,5,2,4,1,5,3,5,1,4,5,3,1,4,5,2,4,5,3,1,2,5,1,2,2,1,5,5,1,1,1,4,2,5,4,3,3,1,3,4,1,1,2,2,2,5,4,4,3,2,1,1,1,1,2,5,1,3,2,1,4,4,2,1,4,5,2,5,5,3,3,1,3,2,2,3,4,1,3,1,5,4,2,5,2,4,1,5,1,4,5,1,2,4,4,1,4,1,4,4,2,2,5,4,1,3,1,3,3,1,5,1,5,5,5,1,3,1,2,1,4,5,4,4,1,3,3,1,4,1,2,1,3,2,1,5,5,3,3,1,3,5,1,5,3,5,3,1,1,1,1,4,4,3,5,5,1,1,2,2,5,5,3,2,5,2,3,4,4,1,1,2,2,4,3,5,5,1,1,5,4,3,1,3,1,2,4,4,4,4,1,4,3,4,1,3,5,5,5,1,3,5,4,3,1,3,5,4,4,3,4,2,1,1,3,1,1,2,4,1,4,1,1,1,5,5,1,3,4,1,1,5,4,4,2,2,1,3,4,4,2,2,2,3"
    val initialLanternfishes = input.split(",").map(Integer.parseInt).map(Lanternfish)
    val initialState = (initialLanternfishes, 0)

    println(s"Initial state: ${printLanterfishes(initialLanternfishes)}")

    val (lanternfishes, day) = (1 to 256)
      .foldLeft(initialState) { (state, _) =>
        val (lanterfishes, day) = state

        val evolution = lanterfishes.map(_.nextDay())
        val existingFishes = evolution.map(_._1)
        val newlyBornFishes = evolution.flatMap(_._2)
        val newLanterfishes = existingFishes ++ newlyBornFishes
        val nextDay = day + 1
        // println(s"After $nextDay days: ${printLanterfishes(newLanterfishes)}")
        (newLanterfishes, nextDay)
      }

    println(s"Number of lanternfishes after $day days: ${lanternfishes.length}")
  }

  private def printLanterfishes(lanterfishes: Seq[Lanternfish]) = lanterfishes.map(_.daysUntilReproduction).mkString(",")

}
