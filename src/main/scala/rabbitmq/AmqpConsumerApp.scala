package org.example.testsscala
package rabbitmq

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.alpakka.amqp.scaladsl.{AmqpSource, CommittableReadResult}
import akka.stream.alpakka.amqp.{AmqpCredentials, AmqpDetailsConnectionProvider, NamedQueueSourceSettings, QueueDeclaration}
import akka.stream.scaladsl.{Sink, Source}

import java.nio.charset.StandardCharsets
import scala.concurrent.ExecutionContext

object AmqpConsumerApp {

  private val connectionProvider = AmqpDetailsConnectionProvider("localhost", 5672)
    .withCredentials(AmqpCredentials("guest", "guest"))
    .withVirtualHost("/")
    .withConnectionName("test-amqp-consumer")

  private val queueName = "sample-queue"

  private val queueDeclaration = QueueDeclaration(queueName).withDurable(true)

  private implicit val system: ActorSystem = ActorSystem("amqp-stream")

  private implicit val ec: ExecutionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    Thread.currentThread().setUncaughtExceptionHandler { (thread, e) =>
      println(e)
    }

    val amqpSource: Source[CommittableReadResult, NotUsed] =
      AmqpSource.committableSource(
        settings = NamedQueueSourceSettings(connectionProvider, queueName)
          .withDeclaration(queueDeclaration)
          .withAckRequired(true),
        bufferSize = 10
      )

    amqpSource
      // .take(1)
      .wireTap { result =>
        println(s"Got some result: $result")
      }
      .map { result =>
        val message = result.message.bytes.decodeString(StandardCharsets.UTF_8)
        (result, message)
      }
      .wireTap { tuple =>
        tuple match {
          case (result, message) =>
            println(s"Got some message: $message")

            val props: Map[String, Any] = Map(
              "redelivered?" -> result.message.envelope.isRedeliver,
              "delivery tag" -> result.message.envelope.getDeliveryTag,
              "exchange" -> result.message.envelope.getExchange,
              "routing key" -> result.message.envelope.getRoutingKey,
              "routing key" -> result.message.envelope.getRoutingKey,
              "correlation ID" -> result.message.properties.getCorrelationId
            )

            val propsStr = props
              .map {
                case (key, value) => s" -- $key -- $value"
              }
              .mkString("\n")

            println(propsStr)
        }
      }
      .mapAsync(parallelism = 1) {
        case (result, _) =>
          val deliveryTag = result.message.envelope.getDeliveryTag
          if (deliveryTag % 5 != 0) {
            result.ack()
          } else {
            result.nack(requeue = true)
          }
      }
      .runWith(Sink.ignore)

    // Await.result(system.whenTerminated, Duration.Inf)
  }

}
