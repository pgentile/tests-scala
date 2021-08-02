package org.example.testsscala
package order.order.transport

sealed trait ServiceItem {
  val id: ServiceItemId
}

case class TrainTransportService(
  id: ServiceItemId = ServiceItemId(),
  passengers: Seq[Passenger]
) extends ServiceItem

case class CardPurchase(id: ServiceItemId) extends ServiceItem
