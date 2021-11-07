package org.example.testsscala
package futuretests

import logging._

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers._

import java.util.UUID
import scala.concurrent._

class FutureSpec extends AsyncFlatSpec {

  private val logger = Logger[this.type]

  def generateId(): Future[String] = Future {
    val id = UUID.randomUUID().toString
    logger.info(m"Generated ID is $id")
    id
  }

  def generateIdTuple(): Future[(String, String)] = generateId().zip(generateId())

  it should "generate tuple with unique IDs" in {
    generateIdTuple()
      .map { ids =>
        logger.info(m"IDs are $ids")
        ids
      }
      .map { case (left, right) =>
        left should have length right.length
        left should not be right
      }
  }

}
