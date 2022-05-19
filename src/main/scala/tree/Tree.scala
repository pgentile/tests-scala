package org.example.testsscala
package tree

sealed trait Tree[+A] {

  def map[B](f: A => B): Tree[B]

}

case class Leaf[+A] private(private val value: A) extends Tree[A] {

  override def map[B](f: A => B): Tree[B] = Leaf(f(value))

  override def toString: String = s"Leaf($value)"

}

case class Branch[+A] private(private val leaves: Seq[Tree[A]]) extends Tree[A] {

  override def map[B](f: A => B): Tree[B] = new Branch(leaves.map(tree => tree.map(f)))

  override def toString: String = s"Branch(${leaves.mkString(", ")})"

}

object Branch {

  def apply[A](first: Tree[A], remaining: Tree[A]*): Branch[A] = new Branch[A](first +: remaining)

}
