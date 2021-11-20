package org.example.testsscala
package actors

import akka.actor.typed.{Behavior, ActorRef}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

class ResponderActor(context: ActorContext[ResponderMessage]) extends AbstractBehavior[ResponderMessage](context) {

  override def onMessage(msg: ResponderMessage): Behavior[ResponderMessage] = {
    msg match {
      case HelloYouResponderMessage => println(s"Bonjour, toi [depuis l'acteur ${context.self.path}]")
    }
    Behaviors.same
  }

}
