package org.example.testsscala
package ruleevalurator

trait RuleEvaluator[A] extends (A => EvaluationResult) {

  def &&(other: RuleEvaluator[A]): RuleEvaluator[A] = (context: A) => apply(context) && other(context)

  def ||(other: RuleEvaluator[A]): RuleEvaluator[A] = (context: A) => apply(context) || other(context)

  def unary_! : RuleEvaluator[A] = (context: A) => !apply(context)

}

object RuleEvaluator {

  def apply[A](f: A => EvaluationResult): RuleEvaluator[A] = (context: A) => f(context)

  def fromBooleanF[A](f: A => Boolean): RuleEvaluator[A] = (context: A) => EvaluationResult.fromBoolean(f(context))

  def fromOptionF[A](f: A => Option[Boolean]): RuleEvaluator[A] = (context: A) => EvaluationResult.fromOption(f(context))

  def always[A](result: EvaluationResult): RuleEvaluator[A] = _ => result

}
