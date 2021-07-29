package org.example.testsscala
package collections

object CollectionsMain {

  def main(args: Array[String]): Unit = {
    val m1 = Map(
      "A" -> 1,
      "B" -> 3,
      "C" -> 8
    )

    val m2 = Map(
      "X" -> 'x'
    )

    val l1 = Seq("A", "B", "C")
    val l2 = "X" +: "Y" +: "Z" +: l1
    val l3 = "A" :: "B" :: Nil

    val v1 = IndexedSeq("A", "B", "C")
    val v2 = v1 :+ 2

    val iterable1 = Iterable("X", "Y", "Z")

    println(m1 ++ m2)
    println(l2)
    println(l3)
    println(v1)
    println(v2)
    println(iterable1)

    l2 foreach { item => println(s"Item is $item")}
  }

}
