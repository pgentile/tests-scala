package org.example.testsscala
package order.order.transport

import order.IdGenerator

case class PassengerId(value: String = IdGenerator.generate("pax_")) extends AnyVal
