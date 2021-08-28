package org.example.testsscala
package workwithimplicits

trait Commandable {

  def run(): Unit

}


object Helpers {

  implicit class SeqExt(s: Seq[_]) {

    def runCommands(implicit commandable: Commandable): Unit = {
      s.foreach(_ => commandable.run())
    }

  }

}
