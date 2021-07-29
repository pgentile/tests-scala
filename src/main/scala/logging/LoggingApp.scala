package org.example.testsscala
package logging

object LoggingApp extends App {

  private val logger = Logger[this.type]

  logger.info("Bonjour, toi")
  logger.error("J'ai planté...", new RuntimeException("Oh, no :("))

}
