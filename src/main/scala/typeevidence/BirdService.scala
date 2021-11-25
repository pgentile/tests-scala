package org.example.testsscala
package typeevidence

class BirdService {

  def aboutBird[A](bird: A)(implicit ev: A <:< Bird): Unit = {
    println(s"About bird: ${bird.name}")
  }

  def aboutParrot[A](parrot: A)(implicit ev: A =:= Parrot): Unit = {
    println(s"About parrot: ${parrot.name}")
  }

}
