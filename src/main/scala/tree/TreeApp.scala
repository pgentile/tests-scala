package org.example.testsscala
package tree

object TreeApp extends App {

  val tree = Branch(
    Leaf(1),
    Branch(
      Leaf(2),
      Leaf(3),
      Branch(
        Leaf(4),
        Leaf(5)
      )
    ),
    Leaf(6)
  )

  private val newTree: Tree[String] = for (value <- tree) yield value.toString

  println(tree.map(_ * 10.4))
  println(newTree)

}
