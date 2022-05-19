package org.example.testsscala
package tree2

object Tree2App {

  def main(args: Array[String]): Unit = {
    val tree = BinaryTree.fromSeq(Seq(1, 3, 4, 5, 9, 10, -4, 7, 6))
    println(tree)
  }

}
