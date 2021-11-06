package org.example.testsscala
package cats.either

sealed trait FunctionalError
case object DeadError extends FunctionalError
case object NotActiveError extends FunctionalError
case object BadDateError extends FunctionalError
