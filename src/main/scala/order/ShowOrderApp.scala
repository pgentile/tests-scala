package org.example.testsscala
package order

import order.{Order, OrderId, OrderReference}
import order.transport.{Passenger, PassengerId, TrainTransportService, ServiceItemId}
import privacy.Privacy

import person._

import scala.language.implicitConversions

object ShowOrderApp extends App {

  1 to 15 foreach { _ =>
    showOrder()
  }

  private def showOrder(): Unit = {
    val order = Order(
      reference = OrderReference("ABC123"),
      trainTransportServices = Seq(
        TrainTransportService(
          passengers = Seq(
            Passenger(
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

  implicit private def toPrivacy(value: FullName): Privacy[FullName] = Privacy(value)

  implicit private def toPrivacy(value: Email): Privacy[Email] = Privacy(value)

  implicit private def toPrivacy(value: PhoneNumber): Privacy[PhoneNumber] = Privacy(value)

  implicit private def toPrivacy[A](value: A): Privacy[A] = Privacy(value)

}
