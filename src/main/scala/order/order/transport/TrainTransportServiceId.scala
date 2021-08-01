package org.example.testsscala
package order.order.transport

import order.IdGenerator

case class TrainTransportServiceId(value: String = IdGenerator.generate("tts_")) extends AnyVal
