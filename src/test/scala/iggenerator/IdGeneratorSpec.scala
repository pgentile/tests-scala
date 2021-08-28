package org.example.testsscala
package iggenerator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import scala.util.Using

class IdGeneratorSpec extends AnyFlatSpec {

  private val queueSize = 100

  def withGenerator(block: IdGenerator => Any): Unit = {
    val idGenerator = new IdGenerator(queueSize)
    Using.resource(idGenerator)(block)
  }

  "the generator" should "give pre-generated IDs" in withGenerator { idGenerator =>
    val count = 20
    val results = (1 to count).map(_ => idGenerator.next)

    results should have size count
    results.toSet should have size count
  }

  "close method" should "stop the generator thread, but generation continues in main thread" in withGenerator { idGenerator =>
    idGenerator.close()

    val count = queueSize + 5
    val results = (1 to count).map(_ => idGenerator.next)

    results should have size count
    results.toSet should have size count
  }

}
