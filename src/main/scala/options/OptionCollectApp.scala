package org.example.testsscala
package options

object OptionCollectApp {

  def main(args: Array[String]): Unit = {
    Seq("Jean Bon", "Guy Gnol").map(Some(_)).foreach { maybeName =>
      val result = maybeName.collect {
        case s"Jean ${lastName}" => lastName
      }
      println(s"Last name for $maybeName: $result")
    }
  }

}
