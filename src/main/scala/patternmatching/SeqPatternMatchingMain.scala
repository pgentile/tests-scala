package org.example.testsscala
package patternmatching

import scala.collection.immutable.LinearSeq

object SeqPatternMatchingMain {

  def main(args: Array[String]): Unit = {
    {
      val emptyList = Seq.empty[Int]
      val one = 4 :: Nil
      val many = LinearSeq(1, 2, 3, 5, 8, 13, 21)
      val two = many.slice(0, 2)

      val target: Seq[Int] = two

      val result = target match {
        case Nil => "Empty"
        case first +: second +: remaining => s"$first, $second and ${remaining.length} remaining items"
        case _ => "Unknown"
      }

      println(result)
    }
    {
      val target = Seq(1, 2, 3)
      val result = target match {
        case _beginning :+ last => s"Last is $last"
      }
      println(result)
    }
  }

}
