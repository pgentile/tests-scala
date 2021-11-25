package org.example.testsscala
package typeevidence

object BirdApp extends App {

  val birdService = new BirdService
  birdService.aboutBird(new Parrot())
  birdService.aboutBird(new Cockatoo())
  birdService.aboutBird(new Canary())
  birdService.aboutParrot(new Parrot())
  birdService.aboutParrot((new Cockatoo()).asInstanceOf[Parrot])

}
