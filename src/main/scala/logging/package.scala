package org.example.testsscala

package object logging {

  implicit class LoggerMessageInterpolator(private val sc: StringContext) extends AnyVal {

    def m(args: Any*): Message = Message(sc, args)

  }

  implicit class StringToMessageExt(private val s: String) extends AnyVal {

    def toLogMessage: Message = Message(s)

  }

}
