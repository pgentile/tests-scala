package org.example.testsscala
package stronglytypedcards

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder


object CardBundleApp {

  private implicit val actionLinkEncoder: Encoder[Action.Link] = deriveEncoder
  private implicit val actionSelectEncoder: Encoder[Action.Select] = deriveEncoder

  private implicit val actionEncoder: Encoder[Action] = Encoder.instance {
    case link: Action.Link => actionLinkEncoder(link).mapObject {
      _.add("type", Encoder.encodeString("LINK"))
    }
    case select: Action.Select => actionSelectEncoder(select).mapObject {
      _.add("type", Encoder.encodeString("SELECT"))
    }
  }

  private implicit val cardEncoder: Encoder[Card[Action]] = deriveEncoder
  private implicit val cardBundleEncoder: Encoder[CardBundle[Action]] = deriveEncoder

  private implicit val cardLinkEncoder: Encoder[Card[Action.Link]] = deriveEncoder
  private implicit val cardLinkBundleEncoder: Encoder[CardBundle[Action.Link]] = deriveEncoder

  def main(args: Array[String]): Unit = {
    printBundle(mixedCardBundle())
    printBundle(linkCardBundle())
    printBundle(selectCardBundle())

    println()
    println("*".repeat(30))
    println()

    println(cardBundleEncoder(mixedCardBundle()).spaces2)
    println(cardBundleEncoder(linkCardBundle()).spaces2)

    println()
    println("*".repeat(30))
    println()

    println(cardLinkBundleEncoder(linkCardBundle()).spaces2)
  }

  private def printBundle(bundle: CardBundle[Action]): Unit = {
    println(s"Bundle ${bundle.title}")
    bundle.cards.foreach { card =>
      println(s"  Card ${card.title}")
      card.actions.foreach { action =>
        println(s"    Action of class ${action.getClass.getSimpleName}")
      }
    }
  }

  private def mixedCardBundle(): CardBundle[Action] =
    CardBundle(
      title = "Mixed card bundle",
      cards = Seq(
        Card(
          title = "Card 1",
          actions = Seq(
            Action.Select(title = "Select 1-1"),
            Action.Link(title = "Link 1-2", url = "http://example.org/2"),
            Action.Link(title = "Link 1-3", url = "http://example.org/3"),
            Action.Select(title = "Select 1-4")
          )
        ),
        Card(
          title = "Card 2",
          actions = Seq(
            Action.Select(title = "Select 2-1"),
            Action.Link(title = "Link 2-2", url = "http://example.org/2"),
            Action.Link(title = "Link 2-3", url = "http://example.org/3")
          )
        )
      )
    )

  private def linkCardBundle(): CardBundle[Action.Link] =
    CardBundle(
      title = "Link card bundle",
      cards = Seq(
        Card(
          title = "Card 1",
          actions = Seq(
            Action.Link(title = "Link 1-2", url = "http://example.org/2"),
            Action.Link(title = "Link 1-3", url = "http://example.org/3")
          )
        )
      )
    )

  private def selectCardBundle(): CardBundle[Action.Select] =
    CardBundle(
      title = "Select card bundle",
      cards = Seq(
        Card(
          title = "Card 1",
          actions = Seq(
            Action.Select(title = "Select 1-1"),
            Action.Select(title = "Select 1-4")
          )
        )
      )
    )

}
