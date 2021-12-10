package org.example.testsscala
package partial

case object PartialApp {

  def hello(firstName: String, lastName: String): Unit = println(s"Bonjour, $firstName $lastName !")

  def genericHello: String = {
    val output = "Bonjour mon pote"
    println(output)
    output
  }

  def curriedHello(firstName: String)(lastName: String): Unit = println(s"Bonjour, ($firstName) ($lastName) !")

  def main(args: Array[String]): Unit = {
    val bonjourJean: String => Unit = hello("Jean", _)

    val aaa: (String, String) => Unit = hello
    aaa("Guy", "Gnol")

    bonjourJean("Bon")
    bonjourJean("Khul")

    val x = genericHello
    val y: () => String = genericHello _

    curriedHello("Debbie")("Loss")


    def abc(a: String, b: String, c: String) = ???
    val curriedAbc: String => String => String => Nothing = (abc _).curried

    val helloDebbie: String => Unit = curriedHello("Debbie")
  }

}
