package org.example.testsscala
package order

case class OrderId(value: String = IdGenerator.generate("o_")) extends AnyVal
