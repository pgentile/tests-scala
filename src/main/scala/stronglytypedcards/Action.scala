package org.example.testsscala
package stronglytypedcards

sealed trait Action

object Action {

  // case class Link
  case class Link(title: String, url: String) extends Action
  case class Select(title: String) extends Action

}
