package org.example.testsscala
package iggenerator

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{ArrayBlockingQueue, BlockingQueue}

class IdGenerator(queueSize: Int = 200) extends AutoCloseable {

  private val queue: BlockingQueue[String] = new ArrayBlockingQueue(queueSize)

  private val thread: Thread = new Thread(() =>
    try {
      while (true) {
        queue.put(generateId)
      }
    } catch {
      case _: InterruptedException => ()
    }
  )

  thread.setName(s"id-generator-${IdGenerator.nextInstanceCounter}")
  thread.setDaemon(true)
  thread.setPriority(Thread.MIN_PRIORITY)
  thread.start()

  def next: String = Option(queue.poll()).getOrElse(generateId)

  override def close(): Unit = {
    thread.interrupt()
    thread.join()
  }

  private def generateId = UUID.randomUUID().toString

}

private object IdGenerator {

  private[this] val instanceCounter = new AtomicInteger()

  @inline
  private def nextInstanceCounter: Int = instanceCounter.incrementAndGet()

}