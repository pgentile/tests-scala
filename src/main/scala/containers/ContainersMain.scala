package org.example.testsscala
package containers

object ContainersMain {

  def main(args: Array[String]): Unit = {
    val cat = new Container(new Cat())
    val dog = new Container(new Dog())

    makeSound(cat)
    makeSound(dog)
  }

  def makeSound(container: Container[Animal]): Unit = {
    container.apply(_.makeNoise())
  }

}
