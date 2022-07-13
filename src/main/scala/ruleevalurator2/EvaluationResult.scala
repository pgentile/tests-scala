package org.example.testsscala
package ruleevalurator2

sealed trait EvaluationResult {

  def &&(other: EvaluationResult): EvaluationResult

  def ||(other: EvaluationResult): EvaluationResult

  def unary_! : EvaluationResult

  def toOption: Option[Boolean]

}

object EvaluationResult {

  sealed abstract class KnownEvaluationResult(value: Boolean) extends EvaluationResult {

    override def toOption: Option[Boolean] = Some(value)

    def &&(other: KnownEvaluationResult): KnownEvaluationResult

    def ||(other: KnownEvaluationResult): KnownEvaluationResult

  }

  object KnownEvaluationResult {

    def unapply(result: KnownEvaluationResult): Option[Boolean] = result.toOption

  }


  case object True extends KnownEvaluationResult(true) {

    override def &&(other: EvaluationResult): EvaluationResult = other match {
      case True => True
      case False => False
      case Unknown => Unknown
    }

    override def ||(other: EvaluationResult): EvaluationResult = True

    override def unary_! : EvaluationResult = False

    override def &&(other: KnownEvaluationResult): KnownEvaluationResult = other

    override def ||(other: KnownEvaluationResult): KnownEvaluationResult = True

  }

  case object False extends KnownEvaluationResult(false) {

    override def &&(other: EvaluationResult): EvaluationResult = False

    override def ||(other: EvaluationResult): EvaluationResult = other match {
      case True => True
      case False => False
      case Unknown => Unknown
    }

    override def unary_! : EvaluationResult = True

    override def &&(other: KnownEvaluationResult): KnownEvaluationResult = False

    override def ||(other: KnownEvaluationResult): KnownEvaluationResult = other

  }

  case object Unknown extends EvaluationResult {

    override def &&(other: EvaluationResult): EvaluationResult = other match {
      case True => Unknown
      case False => False
      case Unknown => Unknown
    }

    override def ||(other: EvaluationResult): EvaluationResult = other match {
      case True => True
      case False => Unknown
      case Unknown => Unknown
    }

    override def unary_! : EvaluationResult = Unknown

    override def toOption: Option[Boolean] = None

  }

  def fromBoolean(value: Boolean): EvaluationResult =
    if (value) True else False

}
