package org.example.testsscala
package people

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

class NameSpec extends AnyFlatSpec {

  "names" should "be ordered by last name" in {
    val names = FakeNameGenerator.take(50).toSeq.sorted
    names.map(_.lastName) shouldBe sorted
  }

}
