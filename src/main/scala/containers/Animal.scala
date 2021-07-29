package org.example.testsscala
package containers

trait Animal {
  def makeNoise(): String
}

class Cat extends Animal {
  override def makeNoise(): String = "Miaou"
}

class Dog extends Animal {
  override def makeNoise(): String = "Ouaf, ouaf"
}
