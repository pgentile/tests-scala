package org.example.testsscala
package ruleevalurator2

import cats.data.NonEmptyList

sealed trait PartialEvaluation[-A] {

  def result: EvaluationResult

  def remaining: Option[Rule[A]]

  def &&[B <: A](other: PartialEvaluation[B]): PartialEvaluation[B]

  def ||[B <: A](other: PartialEvaluation[B]): PartialEvaluation[B]

}

object PartialEvaluation {

  case class CompleteEvaluation[-A](private val res: EvaluationResult.KnownEvaluationResult) extends PartialEvaluation[A] {

    override def result: EvaluationResult.KnownEvaluationResult = res

    override def remaining: Option[Nothing] = None

    override def &&[B <: A](other: PartialEvaluation[B]): PartialEvaluation[B] = {
      (result, other) match {
        case (currentResult, CompleteEvaluation(otherResult)) => CompleteEvaluation(currentResult && otherResult)
        case (EvaluationResult.True, UnknownEvaluation(_)) => other
        case (EvaluationResult.False, UnknownEvaluation(_)) => this
      }
    }

    override def ||[B <: A](other: PartialEvaluation[B]): PartialEvaluation[B] = {
      (result, other) match {
        case (currentResult, CompleteEvaluation(otherResult)) => CompleteEvaluation(currentResult || otherResult)
        case (EvaluationResult.True, UnknownEvaluation(_)) => this
        case (EvaluationResult.False, UnknownEvaluation(_)) => other
      }
    }
  }

  case class UnknownEvaluation[-A](private val rule: Rule[A]) extends PartialEvaluation[A] {

    override def result: EvaluationResult = EvaluationResult.Unknown

    override def remaining: Option[Rule[A]] = Some(rule)

    override def &&[B <: A](other: PartialEvaluation[B]): PartialEvaluation[B] = {
      other match {
        case CompleteEvaluation(otherResult) =>
          otherResult match {
            case EvaluationResult.True => this
            case EvaluationResult.False => other
          }
        case UnknownEvaluation(otherRule) =>
          UnknownEvaluation(AndGroupRule(NonEmptyList.one(rule)) && otherRule)
      }
    }

    override def ||[B <: A](other: PartialEvaluation[B]): PartialEvaluation[B] = ???

  }

}
