package org.example.testsscala
package ruleevalurator

sealed trait EvaluationResult {

  def &&(other: EvaluationResult): EvaluationResult

  def ||(other: EvaluationResult): EvaluationResult

  def unary_! : EvaluationResult

  def toOption: Option[Boolean]

}

object EvaluationResult {

  case object True extends EvaluationResult {

    override def &&(other: EvaluationResult): EvaluationResult = other match {
      case True => True
      case False => False
      case Unknown => Unknown
    }

    override def ||(other: EvaluationResult): EvaluationResult = True

    override def unary_! : EvaluationResult = False

    override def toOption: Option[Boolean] = Some(true)
  }

  case object False extends EvaluationResult {

    override def &&(other: EvaluationResult): EvaluationResult = False

    override def ||(other: EvaluationResult): EvaluationResult = other match {
      case True => True
      case False => False
      case Unknown => Unknown
    }

    override def unary_! : EvaluationResult = True

    override def toOption: Option[Boolean] = Some(false)

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

  def fromOption(opt: Option[Boolean]): EvaluationResult =
    opt.map(fromBoolean).getOrElse(Unknown)

}
