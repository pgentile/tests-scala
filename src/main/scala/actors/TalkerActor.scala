package org.example.testsscala
package actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

class TalkerActor(context: ActorContext[GreetingMessage], mainResponder: ActorRef[ResponderMessage]) extends AbstractBehavior[GreetingMessage](context) {

  override def onMessage(msg: GreetingMessage): Behavior[GreetingMessage] = {
    msg match {
      case HelloMessage(name) =>
        println(s"Bonjour, $name")
        mainResponder ! HelloYouResponderMessage

        val responder = context.spawnAnonymous(Behaviors.setup(new ResponderActor(_)))
        responder ! HelloYouResponderMessage
      case HowAreYouMessage => println("Comment ça va aujourd'hui ?")
      case GoodbyeMessage => println("Au revoir")
    }
    this
  }

}
