package org.example.testsscala
package traits

object TraitApp extends App {

  val command = new ExampleCommand
  command.execute.executeWithCredentials.execute.executeWithCredentials

}
