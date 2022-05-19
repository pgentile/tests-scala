package org.example.testsscala
package stronglytypedcards

case class Card[A <: Action](title: String, actions: Seq[A])
