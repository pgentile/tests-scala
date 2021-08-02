package org.example.testsscala
package order.order

import order.order.transport.TrainTransportService

final case class Order(
  id: OrderId = OrderId(),
  reference: OrderReference,
  trainTransportServices: Seq[TrainTransportService] = Seq.empty
)
