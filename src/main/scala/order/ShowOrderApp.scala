package org.example.testsscala
package order

import order.{Order, OrderId, OrderReference}
import order.transport.{Passenger, PassengerId, TrainTransportService, TrainTransportServiceId}
import privacy.Privacy

import person._

import scala.language.implicitConversions

object ShowOrderApp extends App {

  implicit def toPrivacy[A](value: A): Privacy[A] = Privacy(value)

  private def showOrder(): Unit = {
    val order = Order(
      id = OrderId(),
      reference = OrderReference("ABC123"),
      trainTransportServices = Seq(
        TrainTransportService(
          id = TrainTransportServiceId(),
          passengers = Seq(
            Passenger(
              id = PassengerId(),
              name = Some(FullName("Jean", "Bon")),
              email = Some(Email("jeanbon@example.org")),
              phoneNumber = Some(PhoneNumber("+33600112233"))
            )
          )
        )
      )
    )

    println(order)
  }

  1 to 15 foreach { _ =>
    showOrder()
  }

}
