package org.example.testsscala
package stronglytypedcards

case class CardBundle[A <: Action](cards: Seq[Card[A]])
