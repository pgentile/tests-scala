package org.example.testsscala
package order.order

import order.IdGenerator

case class OrderId(value: String = IdGenerator.generate("o_")) extends AnyVal
