package org.example.testsscala

package object validation {

  type Validation[A] = Either[Seq[ValidationError], A]

  implicit class ValidationHelper[A](val validation: Validation[A]) {

    def addError(error: ValidationError): Validation[A] = {
      validation match {
        case Left(errors) => Left(errors :+ error)
        case _ => Left(Seq(error))
      }
    }

  }

}
