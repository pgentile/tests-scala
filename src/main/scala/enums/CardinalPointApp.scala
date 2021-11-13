package org.example.testsscala
package enums

object CardinalPointApp extends App {

  println(s"Cardinal points are: ${CardinalPoint.values}")

  CardinalPoint.values.foreach { point =>
    println(s"${point.entryName}: ${point.direction}°")
  }

}
