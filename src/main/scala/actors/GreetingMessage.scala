package org.example.testsscala
package actors

sealed trait GreetingMessage
case class HelloMessage(name: String) extends GreetingMessage
case object GoodbyeMessage extends GreetingMessage
case object HowAreYouMessage extends GreetingMessage
