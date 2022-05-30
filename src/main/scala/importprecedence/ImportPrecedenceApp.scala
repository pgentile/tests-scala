package org.example.testsscala
package importprecedence

import ExportsA._
import ExportsB._

object ImportPrecedenceApp {

  def main(args: Array[String]): Unit = {
    // doesn't compile: ambiguous implicit values
    // printExample()
  }

  def printExample()(implicit example: Example): Unit = println(s"Example = $example")

}
