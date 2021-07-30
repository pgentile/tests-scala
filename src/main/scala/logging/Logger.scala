package org.example.testsscala
package logging

import org.slf4j.{LoggerFactory => Slf4jLoggerFactory}

import scala.reflect.ClassTag

class Logger private(name: String) {

  private val wrapped = Slf4jLoggerFactory.getLogger(name)

  def trace(message: => Message): Unit = {
    if (wrapped.isTraceEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.trace(format, args: _*)
    }
  }

  def trace(message: => Message, e: Throwable): Unit = {
    if (wrapped.isTraceEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.trace(format, args :+ e: _*)
    }
  }

  def debug(message: => Message): Unit = {
    if (wrapped.isDebugEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.debug(format, args: _*)
    }
  }

  def debug(message: => Message, e: Throwable): Unit = {
    if (wrapped.isDebugEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.debug(format, args :+ e: _*)
    }
  }

  def info(message: => Message): Unit = {
    if (wrapped.isInfoEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.info(format, args: _*)
    }
  }

  def info(message: => Message, e: Throwable): Unit = {
    if (wrapped.isInfoEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.info(format, args :+ e: _*)
    }
  }

  def warn(message: => Message): Unit = {
    if (wrapped.isWarnEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.warn(format, args: _*)
    }
  }

  def warn(message: => Message, e: Throwable): Unit = {
    if (wrapped.isWarnEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.warn(format, args :+ e: _*)
    }
  }

  def error(message: => Message): Unit = {
    if (wrapped.isErrorEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.error(format, args: _*)
    }
  }

  def error(message: => Message, e: Throwable): Unit = {
    if (wrapped.isErrorEnabled) {
      val (format, args) = extractFromMessage(message)
      wrapped.error(format, args :+ e: _*)
    }
  }

  private def extractFromMessage(messageFactory: => Message): (String, Seq[Any]) = {
    val message = messageFactory
    (message.format, message.args)
  }

}

object Logger {

  def apply[T](name: String): Logger = new Logger(name)

  def apply(clazz: Class[_]): Logger = Logger(clazz.getName)

  def apply[T](implicit classTag: ClassTag[T]): Logger = Logger(classTag.runtimeClass)

}
