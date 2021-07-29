package org.example.testsscala
package logging

import org.slf4j.{LoggerFactory => Slf4jLoggerFactory}

import scala.reflect.ClassTag

class Logger(clazz: Class[_]) {

  private val wrapped = Slf4jLoggerFactory.getLogger(clazz)

  def debug(message: => String): Unit = {
    if (wrapped.isDebugEnabled) {
      wrapped.debug(message)
    }
  }

  def debug(message: => String, e: Throwable): Unit = {
    if (wrapped.isDebugEnabled) {
      wrapped.debug(message, e)
    }
  }

  def info(message: => String): Unit = {
    if (wrapped.isInfoEnabled) {
      wrapped.info(message)
    }
  }

  def info(message: => String, e: Throwable): Unit = {
    if (wrapped.isInfoEnabled) {
      wrapped.info(message, e)
    }
  }

  def warn(message: => String): Unit = {
    if (wrapped.isWarnEnabled) {
      wrapped.warn(message)
    }
  }

  def warn(message: => String, e: Throwable): Unit = {
    if (wrapped.isWarnEnabled) {
      wrapped.warn(message, e)
    }
  }

  def error(message: => String): Unit = {
    if (wrapped.isErrorEnabled) {
      wrapped.error(message)
    }
  }

  def error(message: => String, e: Throwable): Unit = {
    if (wrapped.isErrorEnabled) {
      wrapped.error(message, e)
    }
  }

}

object Logger {

  def apply[T](implicit classTag: ClassTag[T]): Logger = new Logger(classTag.runtimeClass)

}
