package org.example.testsscala
package stronglytypedcards

case class CardBundle[+A <: Action](title: String, cards: Seq[Card[A]])

case class Card[+A <: Action](title: String, actions: Seq[A])

sealed trait Action

object Action {

  // case class Link
  case class Link(title: String, url: String) extends Action
  case class Select(title: String) extends Action

}
