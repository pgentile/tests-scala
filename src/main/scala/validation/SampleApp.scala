package org.example.testsscala
package validation

import java.time.LocalDate

object SampleApp extends App {

  val passengerUpdate1 = PassengerUpdate("Jean", "Bon", LocalDate.now)
  println(passengerUpdate1)

}
