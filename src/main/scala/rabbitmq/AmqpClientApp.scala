package org.example.testsscala
package rabbitmq

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.amqp.scaladsl.AmqpSink
import akka.stream.alpakka.amqp.{AmqpCredentials, AmqpDetailsConnectionProvider, AmqpUriConnectionProvider, AmqpWriteSettings, QueueDeclaration}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString

import java.time.LocalDateTime
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

object AmqpClientApp {

  /// %2f => vhost /
  private val connectionProvider = AmqpDetailsConnectionProvider("localhost", 5672)
    .withCredentials(AmqpCredentials("guest", "guest"))
    .withVirtualHost("/")
    .withConnectionName("test-amqp")

  private val queueName = "sample-queue"

  private val queueDeclaration = QueueDeclaration(queueName).withDurable(true)

  private implicit val system: ActorSystem = ActorSystem("amqp-stream")

  private implicit val ec: ExecutionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    val amqpSink: Sink[ByteString, Future[Done]] = AmqpSink.simple(
      AmqpWriteSettings(connectionProvider)
        .withRoutingKey(queueName)
        .withDeclaration(queueDeclaration)
    )

    val r1 = sendMessage(amqpSink)
    val r2 = sendMessage(amqpSink)
    val r3 = sendMessage(amqpSink)

    Await.result(r1, Duration.Inf)
    Await.result(r2, Duration.Inf)
    Await.result(r3, Duration.Inf)
    Await.result(system.terminate(), Duration.Inf)
  }

  private def sendMessage(amqpSink: Sink[ByteString, Future[Done]]): Future[Unit] = {
    Source.repeat(s"Coucou ${LocalDateTime.now}")
      .throttle(1, 1.seconds)
      .map(s => ByteString(s))
      .wireTap(message => println(s"[${Thread.currentThread.getName}] Sent $message"))
      .runWith(amqpSink)
      .map(_ => ())
  }
}
