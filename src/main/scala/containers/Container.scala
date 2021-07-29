package org.example.testsscala
package containers

class Container[+A](val value: A) {

  def apply[B](f: A => B): Unit = {
    println(s"Contained element is ${f(value)}")
  }

}
