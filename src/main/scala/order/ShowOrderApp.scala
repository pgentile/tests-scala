package org.example.testsscala
package order

object ShowOrderApp extends App {

  private def showOrder(): Unit = {
    val order = Order(
      id = OrderId(),
      reference = OrderReference("ABC123")
    )
    println(order)
  }

  1 to 15 foreach { _ =>
    showOrder()
  }

}
