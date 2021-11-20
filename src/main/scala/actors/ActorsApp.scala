package org.example.testsscala
package actors

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object ActorsApp extends App {

  val root = Behaviors.setup[GreetingMessage] { context =>
    val responder = context.spawn(Behaviors.setup(new ResponderActor(_)), "responder")

    val talkers = (1 to 3).map { position =>
      context.spawn(Behaviors.setup(new TalkerActor(_, responder)), s"talker.$position")
    }

    Behaviors.receiveMessage { message =>
      talkers.foreach { talker => talker ! message }
      Behaviors.same
    }
  }

  private val system = ActorSystem(root, "root")

  system ! HelloMessage("Günther")
  system ! HowAreYouMessage

}
