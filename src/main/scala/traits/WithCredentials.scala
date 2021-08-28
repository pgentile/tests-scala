package org.example.testsscala
package traits

trait WithCredentials {
  this: Command =>

  def executeWithCredentials: this.type = {
    addCredentials()
    this.execute
  }

  protected def addCredentials(): Unit

}
