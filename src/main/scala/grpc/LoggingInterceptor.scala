package org.example.testsscala
package grpc

import logging._

import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import io.grpc.{Metadata, ServerCall, ServerCallHandler, ServerInterceptor}
import _root_.scalapb.GeneratedMessage
import _root_.scalapb.json4s.JsonFormat

object LoggingInterceptor extends ServerInterceptor {

  private val logger = Logger[this.type]

  override def interceptCall[ReqT, RespT](call: ServerCall[ReqT, RespT], headers: Metadata, next: ServerCallHandler[ReqT, RespT]): ServerCall.Listener[ReqT] = {
    val callName = call.getMethodDescriptor.getFullMethodName
    logger.info(m"Intercepted call: $callName")

    val listener = next.startCall(call, headers)
    new SimpleForwardingServerCallListener[ReqT](listener) {

      override def onMessage(message: ReqT): Unit = {
        logger.info {
          val requestAsJson = JsonFormat.toJsonString(message.asInstanceOf[GeneratedMessage])
          m"Received request $requestAsJson"
        }
        super.onMessage(message)
      }

      override def onComplete(): Unit = logger.info(m"Call completed for $callName")

    }
  }
}
