package org.example.testsscala
package validation

import java.time.LocalDate

case class PassengerUpdate private(firstName: String, lastName: String, birthDate: LocalDate)

object PassengerUpdate {

  def apply(firstName: String, lastName: String, birthDate: LocalDate): Unvalidated[PassengerUpdate] = {
    Unvalidated(new PassengerUpdate(firstName, lastName, birthDate))
  }

}
