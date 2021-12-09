package org.example.testsscala
package grpc

import logging._

import _root_.grpc.ExampleServiceGrpc.ExampleService
import _root_.grpc.{HelloRequest, HelloResponse, RaiseErrorRequest, RaiseErrorResponse}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import scala.concurrent.Future
import scala.concurrent.duration._

class ExampleServiceImpl extends ExampleService {

  private val logger = Logger[this.type]

  import scala.concurrent.ExecutionContext.Implicits.global

  override def sayHello(request: HelloRequest): Future[HelloResponse] = {
    Future {
      logger.info(m"Called say hello")
      HelloResponse(
        message = s"Bonjour, ${request.name}",
        createdAt = ZonedDateTime.now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
      )
    }
  }

  override def raiseError(request: RaiseErrorRequest): Future[RaiseErrorResponse] = {
    Future {
      logger.info(m"Will raise some error")
      Thread.sleep(3.seconds.toMillis)
      throw new RuntimeException("This is failure")
    }
  }
}
