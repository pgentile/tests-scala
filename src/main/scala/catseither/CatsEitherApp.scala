package org.example.testsscala
package catseither

import logging._

import scala.concurrent.ExecutionContext

object CatsEitherApp extends App {

  private val logger = Logger[this.type]

  implicit private val ec: ExecutionContext = ExecutionContext.global

  val service: Service = ServiceImpl
  service.compute("AAA").foreach { outcome =>
    logger.info(m"Got some outcome: $outcome")
  }

}
