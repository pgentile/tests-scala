package org.example.testsscala
package ruleevalurator

import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

class RuleEvaluatorTest extends AnyFlatSpec {

  case class RuleContext()

  object SampleRules {

    def rule1(context: RuleContext): Boolean = Random.nextBoolean()
    def rule2(context: RuleContext): EvaluationResult = ???


  }

  private val contextToResult: RuleContext => EvaluationResult = SampleRules.rule1 _ && SampleRules.rule2 _

}
