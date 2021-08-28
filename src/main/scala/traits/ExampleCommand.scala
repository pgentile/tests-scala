package org.example.testsscala
package traits

class ExampleCommand extends Command with WithCredentials {

  override def execute: this.type = {
    println("Running my command")
    this
  }

  override protected def addCredentials(): Unit = println("Authentication")

}

