package org.example.testsscala
package grpc

import logging._

import _root_.grpc.ExampleServiceGrpc
import io.grpc.{Server, ServerBuilder}

import scala.concurrent.ExecutionContext


object ExampleServerApp {

  private val logger = Logger[this.type]

  private val port = 1234

  def main(args: Array[String]): Unit = {
    Thread.setDefaultUncaughtExceptionHandler { (thread, e) =>
      logger.error(m"Thread ${thread.getName} interrupted", e)
    }

    val exampleService = new ExampleServiceImpl

    val server: Server = ServerBuilder.forPort(port)
      .addService(ExampleServiceGrpc.bindService(exampleService, ExecutionContext.global))
      .intercept(LoggingInterceptor)
      .build

    sys.addShutdownHook {
      logger.info(m"Shutting down...")
      server.shutdown()
      logger.info(m"Terminated")
    }

    logger.info(m"Awaiting termination...")
    server.start.awaitTermination()
  }

}
