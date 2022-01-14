package org.example.testsscala
package options

object OptionCollectApp {

  def main(args: Array[String]): Unit = {
    Seq("Jean Bon", "Guy Gnol").foreach { name =>
      val result = Some(name).collect {
        case s"Jean ${lastName}" => lastName
      }
      println(s"Last name for $name: $result")
    }
  }

}
