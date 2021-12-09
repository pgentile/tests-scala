package org.example.testsscala
package grpc

import logging._

import _root_.grpc.{ExampleServiceGrpc, HelloRequest, RaiseErrorRequest}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}

import scala.util.{Failure, Success}


object ExampleClientApp {

  private val logger = Logger[this.type]

  private val host = "localhost"
  private val port = 1234

  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {
    val channel: ManagedChannel = ManagedChannelBuilder.forAddress(host, port)
      .usePlaintext
      .build

    val request = HelloRequest(name = "Pierre")

    val stub = ExampleServiceGrpc.stub(channel)

    val barrier = new CountBarrier

    barrier.useSignal { signal =>
      stub.sayHello(request).onComplete { tResponse =>
        tResponse match {
          case Success(response) =>
            logger.info(m"""Response message is: "${response.message}", created at ${response.createdAt}""")
          case Failure(e) =>
            logger.error(m"Failed to call server", e)
        }
        signal()
      }
    }

    barrier.useSignal { signal =>
      stub.raiseError(RaiseErrorRequest()).onComplete { tResponse =>
        tResponse match {
          case Success(_) =>
            logger.info(m"Done")
          case Failure(e) =>
            logger.error(m"Failed to call server", e)
        }
        signal()
      }
    }

    barrier.await()
    logger.info(m"End of client")
  }

  private class CountBarrier {

    private val lock = new Object

    private var count = 0

    def increment(): Unit = {
      lock.synchronized {
        count += 1
      }
    }

    def decrement(): Unit = {
      lock.synchronized {
        count -= 1
        lock.notify()
      }
    }

    def useSignal[A](f: (() => Unit) => A): A = {
      increment()
      f(decrement)
    }

    def await(): Unit = {
      lock.synchronized {
        while (count > 0) {
          lock.wait()
        }
      }
    }

  }

}
