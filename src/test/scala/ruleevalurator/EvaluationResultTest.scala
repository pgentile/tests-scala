package org.example.testsscala
package ruleevalurator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.Assertions._

import EvaluationResult.{True, False, Unknown}

class EvaluationResultTest extends AnyFlatSpec {

  "combinaisons" should "be communative for && operator" in {
    val samples = Seq(
      (True, True, True),
      (True, False, False),
      (True, Unknown, Unknown),
      (False, Unknown, False),
      (False, False, False)
    )

    samples.map { sample =>
      val (left, right, result) = sample
      assert((left && right) == result, s"$left && $right == $result")
      assert((right && left) == result, s"Commutative: $right && $left == $result")
    }

  }

  it should "be communative for || operator" in {
    val samples = Seq(
      (True, True, True),
      (True, False, True),
      (True, Unknown, True),
      (False, Unknown, Unknown),
      (False, False, False)
    )

    samples.map { sample =>
      val (left, right, result) = sample
      assert((left || right) == result, s"$left || $right == $result")
      assert((right || left) == result, s"Commutative: $right || $left == $result")
    }

  }

}
