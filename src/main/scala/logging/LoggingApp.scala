package org.example.testsscala
package logging

import java.util.concurrent.ThreadLocalRandom

object LoggingApp extends App {

  private val logger = Logger[this.type]

  val name = "Pierre"
  val surname = "Gentile"
  logger.info(m"Bonjour, $name $surname")
  logger.info(m"Bonjour, toi, inconnu n° ${Math.abs(ThreadLocalRandom.current.nextInt())}")
  logger.error(m"J'ai planté...", new RuntimeException("Oh, no :("))

}
