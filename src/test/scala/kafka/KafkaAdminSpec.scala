package org.example.testsscala
package kafka

import logging._

import io.github.embeddedkafka.EmbeddedKafka
import org.apache.kafka.clients.admin.OffsetSpec
import org.apache.kafka.clients.consumer.{KafkaConsumer, OffsetAndMetadata}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{Deserializer, Serializer, StringDeserializer, StringSerializer}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters._
import scala.jdk.DurationConverters.ScalaDurationOps

class KafkaAdminSpec extends AnyFlatSpec with EmbeddedKafka {

  private val logger = Logger[this.type]

  private val topicName = "topic1"

  "the admin" should "administer the Kafka cluster" in {
    withRunningKafka {
      createCustomTopic(topic = topicName, partitions = 3).get

      implicit val serializer: Serializer[String] = new StringSerializer()
      implicit val deserializer: Deserializer[String] = new StringDeserializer()

      logger.info(m"Publish some messages with custom producer")

      withProducer[String, String, Unit] { producer =>
        for (index <- 1 to 100) {
          logger.info(m"A - Publishing message with index $index")
          val recordMetadata = producer.send(new ProducerRecord(topicName, s"key-a-$index", s"value-a-$index")).get()
          logger.info(m"Record metadata: $recordMetadata")

          producer.flush()
        }
      }

      logger.info(m"Consume some messages")

      val groupId = withConsumer[String, String, String] { consumer =>
        consumeMessages(consumer)

        // Group ID needed after this step
        consumer.groupMetadata().groupId()
      }

      logger.info(m"Publish for the second time some messages")

      for (index <- 1 to 100) {
        logger.info(m"B - Publishing message with index $index")
        publishToKafka(topicName, s"key-b-$index", s"value-b-$index")
      }

      logger.info(m"Administer the Kafka broker")

      withAdminClient[Unit] { adminClient =>
        val listings = adminClient.listTopics().namesToListings().get()
        logger.info(m"Listings are: $listings")

        val topicDescriptions = adminClient.describeTopics(Seq(topicName)).all().get()
        val topicDescription = topicDescriptions(topicName)
        logger.info(m"Topic description is: $topicDescription")

        val topicPartitionOffsets = topicDescription.partitions().iterator
          .map { partition => new TopicPartition(topicName, partition.partition) -> OffsetSpec.latest }
          .toMap

        adminClient.listOffsets(topicPartitionOffsets).all().get().foreach {
          case partition -> resultInfo => logger.info(m"Result for partition $partition: $resultInfo")
        }

        adminClient.listConsumerGroups().all().get().foreach { listing =>
          logger.info(m"Consumer group is: ${listing.groupId}")
        }

        adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get().foreach {
          case partition -> offsetAndMetadata => logger.info(m"Offsets for group $groupId: $partition: $offsetAndMetadata")
        }

        val consumerGroupOffsetChanges = adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get().asScala
          .map {
            case partition -> offsetAndMetadata => {
              val newOffset = offsetAndMetadata.offset / 2
              partition -> new OffsetAndMetadata(newOffset)
            }
          }
          .toMap

        adminClient.alterConsumerGroupOffsets(groupId, consumerGroupOffsetChanges).all().get()

        adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get().foreach {
          case partition -> offsetAndMetadata => logger.info(m"After update: offsets for group $groupId: $partition: $offsetAndMetadata")
        }
      }

      logger.info(m"Consume for the second time")

      withConsumer[String, String, Unit](consumeMessages)

      logger.info(m"After second consumption")

      withAdminClient[Unit] { adminClient =>
        adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get().foreach {
          case partition -> offsetAndMetadata => logger.info(m"After second consumption: offsets for group $groupId: $partition: $offsetAndMetadata")
        }
      }
    }
  }

  private def consumeMessages(consumer: KafkaConsumer[String, String]): Unit = {
    consumer.subscribe(Seq(topicName))
    try {
      val records = consumer.poll(30.seconds.toJava)
      records.iterator().foreach { record =>
        logger.info(m"Consumed message: from topic ${record.topic}: ${record.key} => ${record.value}")
      }

      logger.info(m"Consumed ${records.count} messages")

      consumer.commitSync()
    } finally {
      consumer.unsubscribe()
    }
  }

  private implicit def javaCollectionToIterable[A](collection: java.util.Collection[A]): Iterable[A] = collection.asScala

  private implicit def javaIteratorToScala[A](iterator: java.util.Iterator[A]): Iterator[A] = iterator.asScala

  private implicit def javaListToScala[A](list: java.util.List[A]): Seq[A] = list.asScala.toSeq

  private implicit def javaMapToScala[K, V](map: java.util.Map[K, V]): Map[K, V] = map.asScala.toMap

  private implicit def scalaSeqToJava[A](seq: Seq[A]): java.util.List[A] = seq.asJava

  private implicit def scalaMapToJava[K, V](map: Map[K, V]): java.util.Map[K, V] = map.asJava


}
