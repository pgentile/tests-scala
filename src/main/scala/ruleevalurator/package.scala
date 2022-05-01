package org.example.testsscala

import scala.language.implicitConversions

package object ruleevalurator {

  implicit def functionToRuleEvaluator[A](f: A => EvaluationResult): RuleEvaluator[A] = RuleEvaluator(f)

  implicit def booleanFunctionToRuleEvaluator[A](f: A => Boolean): RuleEvaluator[A] = RuleEvaluator.fromBooleanF(f)

  implicit def optionFunctionToRuleEvaluator[A](f: A => Option[Boolean]): RuleEvaluator[A] = RuleEvaluator.fromOptionF(f)

}
