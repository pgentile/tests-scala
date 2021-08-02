package org.example.testsscala
package order.order.transport

import order.person.{Email, FullName, PhoneNumber}
import privacy.Privacy

import java.time.LocalDate

case class Passenger(
  id: PassengerId = PassengerId(),
  name: Option[Privacy[FullName]] = None,
  birthDate: Option[LocalDate] = None,
  email: Option[Privacy[Email]] = None,
  phoneNumber: Option[Privacy[PhoneNumber]] = None
)
