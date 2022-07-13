package org.example.testsscala
package ruleevalurator2

import cats.data.NonEmptyList

trait Rule[-A] extends (A => EvaluationResult) {

  def &&[B <: A](other: Rule[B]): Rule[B] = AndGroupRule(NonEmptyList.one(this)) && other

  def ||[B <: A](other: Rule[B]): Rule[B] = OrGroupRule(NonEmptyList.one(this)) || other

  def unary_! : Rule[A] = NegationRule(this)

  def partialEval(input: A): PartialEvaluation[A] = {
    this (input) match {
      case result: EvaluationResult.KnownEvaluationResult => PartialEvaluation.CompleteEvaluation(result)
      case EvaluationResult.Unknown => PartialEvaluation.UnknownEvaluation(this)
    }
  }

}

object Rule {

  def apply[A](f: A => EvaluationResult): Rule[A] =
    input => f(input)

}

case class NegationRule[-A](rule: Rule[A]) extends Rule[A] {

  override def apply(input: A): EvaluationResult = !rule(input)

  override def unary_! : Rule[A] = rule

}

case class AndGroupRule[-A](rules: NonEmptyList[Rule[A]]) extends Rule[A] {

  override def apply(input: A): EvaluationResult = {
    rules.foldLeft[EvaluationResult](EvaluationResult.True) { (accumulator, rule) =>
      accumulator && rule(input)
    }
  }

  override def &&[B <: A](other: Rule[B]): Rule[B] = {
    other match {
      case AndGroupRule(rightRules) => AndGroupRule(rules ::: rightRules)
      case rightRule => AndGroupRule(rules :+ rightRule)
    }
  }

  override def partialEval(input: A): PartialEvaluation[A] = {
    val _bootstrap: (EvaluationResult, List[Rule[A]]) = (EvaluationResult.True, List.empty)
    ???
  }
}

case class OrGroupRule[-A](rules: NonEmptyList[Rule[A]]) extends Rule[A] {

  override def apply(input: A): EvaluationResult = {
    rules.foldLeft[EvaluationResult](EvaluationResult.False) { (accumulator, rule) =>
      accumulator || rule(input)
    }
  }

  override def ||[B <: A](other: Rule[B]): Rule[B] = {
    other match {
      case OrGroupRule(rightRules) => OrGroupRule(rules ::: rightRules)
      case rightRule => OrGroupRule(rules :+ rightRule)
    }
  }

}
