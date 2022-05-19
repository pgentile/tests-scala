package org.example.testsscala
package tree2

import scala.math.Ordering.Implicits._

sealed trait BinaryTree[A] {

  def addValue(newValue: A)(implicit ordering: Ordering[A]): BinaryTree[A]

  def isEmpty: Boolean

}

object BinaryTree {

  def empty[A]: BinaryTree[A] = Empty()

  def fromSeq[A](seq: Seq[A])(implicit ordering: Ordering[A]): BinaryTree[A] = {
    seq.foldLeft(empty[A]) { (tree, value) =>
      tree.addValue(value)
    }
  }

}

case class Branch[A](value: A, left: BinaryTree[A], right: BinaryTree[A]) extends BinaryTree[A] {

  override def addValue(newValue: A)(implicit ordering: Ordering[A]): BinaryTree[A] = {
    if (value == newValue) {
      this
    } else {
      (left.isEmpty, right.isEmpty) match {
        case (true, true) =>
          if (newValue < value) {
            withLeft(Branch.fromValue(newValue))
          } else {
            withRight(Branch.fromValue(newValue))
          }
        case (false, false) =>
          if (newValue < value) {
            withLeft(left.addValue(newValue))
          } else {
            withRight(right.addValue(newValue))
          }
        case (true, false) =>
          if (newValue < value) {
            withLeft(Branch.fromValue(newValue))
          } else {
            withRight(right.addValue(newValue))
          }
        case (false, true) =>
          if (newValue > value) {
            withRight(Branch.fromValue(newValue))
          } else {
            withLeft(left.addValue(newValue))
          }
      }
    }
  }

  override def isEmpty: Boolean = false

  private def withLeft(newLeft: BinaryTree[A]): BinaryTree[A] =
    Branch(value, newLeft, right)

  private def withRight(newRight: BinaryTree[A]): BinaryTree[A] =
    Branch(value, left, newRight)
}

object Branch {

  def fromValue[A](value: A): BinaryTree[A] =
    Branch(value, Empty(), Empty())

}

case class Empty[A]() extends BinaryTree[A] {

  override def addValue(newValue: A)(implicit ordering: Ordering[A]): BinaryTree[A] =
    Branch(newValue, Empty(), Empty())

  override def isEmpty: Boolean = true

}
