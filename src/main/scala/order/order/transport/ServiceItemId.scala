package org.example.testsscala
package order.order.transport

import order.IdGenerator

case class ServiceItemId(value: String = IdGenerator.generate("tts_")) extends AnyVal
