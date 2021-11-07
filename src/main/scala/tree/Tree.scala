package org.example.testsscala
package tree

sealed trait Tree[+A] {

  def map[B](f: A => B): Tree[B]

  def flatMap[B](f: A => Tree[B]): Tree[B]

}

case class Leaf[+A] private(private val value: A) extends Tree[A] {

  override def map[B](f: A => B): Tree[B] = new Leaf(f(value))

  override def flatMap[B](f: A => Tree[B]): Tree[B] = f(value)
}

object Leaf {

  def apply[A](value: A): Tree[A] = new Leaf(value)

}

case class Branch[+A] private(private val leaves: Seq[Tree[A]]) extends Tree[A] {

  override def map[B](f: A => B): Tree[B] = new Branch(leaves.map(tree => tree.map(f)))

  override def flatMap[B](f: A => Tree[B]): Tree[B] = {
    val newLeaves: Seq[Tree[B]] = leaves.map(leaf => leaf.flatMap(f))
    new Branch(newLeaves)
  }

  override def toString: String = s"Branch(${leaves.mkString(", ")})"

}

object Branch {

  def apply[A](first: Tree[A], remaining: Tree[A]*): Tree[A] = new Branch[A](first +: remaining)

}
