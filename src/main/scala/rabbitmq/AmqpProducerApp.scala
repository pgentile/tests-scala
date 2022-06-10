package org.example.testsscala
package rabbitmq

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.amqp.scaladsl.AmqpSink
import akka.stream.alpakka.amqp.{AmqpCachedConnectionProvider, AmqpCredentials, AmqpDetailsConnectionProvider, AmqpUriConnectionProvider, AmqpWriteSettings, QueueDeclaration, WriteMessage}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import com.rabbitmq.client.AMQP.BasicProperties

import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

object AmqpProducerApp {

  /// %2f => vhost /
  private val connectionProvider = AmqpDetailsConnectionProvider("localhost", 5672)
    .withCredentials(AmqpCredentials("guest", "guest"))
    .withVirtualHost("/")
    .withConnectionName("test-amqp-producer")

  private val cachedConnectionProvider = AmqpCachedConnectionProvider(connectionProvider)

  private val queueName = "sample-queue"

  private val queueDeclaration = QueueDeclaration(queueName).withDurable(true)

  private implicit val system: ActorSystem = ActorSystem("amqp-stream")

  private implicit val ec: ExecutionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    val amqpSink: Sink[WriteMessage, Future[Done]] = AmqpSink(
      AmqpWriteSettings(cachedConnectionProvider)
        .withRoutingKey(queueName)
        .withDeclaration(queueDeclaration)
    )

    val r1 = sendMessage(amqpSink)
    // val r2 = sendMessage(amqpSink)
    // val r3 = sendMessage(amqpSink)

    Await.result(r1, Duration.Inf)
    // Await.result(r2, Duration.Inf)
    // Await.result(r3, Duration.Inf)
    Await.result(system.terminate(), Duration.Inf)
  }

  private def sendMessage(amqpSink: Sink[WriteMessage, Future[Done]]): Future[Unit] = {
    Source.tick(1.second, 5.seconds, ())
      .map { _ =>
        s"Coucou ${LocalDateTime.now}"
      }
      .wireTap(message => println(s"[${Thread.currentThread.getName}] Sent $message"))
      .map { payload =>

        val properties = new BasicProperties.Builder()
          .correlationId(UUID.randomUUID().toString.replace("-", ""))
          .build()

        WriteMessage(ByteString(payload)).withProperties(properties)
      }
      .runWith(amqpSink)
      .map(_ => ())
  }
}
