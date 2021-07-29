package org.example.testsscala
package workwithimplicits

object WorkWithImplicits {

  private val requestContext: RequestContext = RequestContext(
    "corrId",
    "reqId",
    Some(Username("xxx"))
  )

  def main(args: Array[String]): Unit = {
    {
      implicit val requestContext: RequestContext = this.requestContext
      val aboutUser = new AboutUser
      println(aboutUser.about)
    }

    {
      val aboutUser = new AboutUser

      // Rendre l'argument reçu implicite dans le contexte
      val callMe = { implicit requestContext: RequestContext =>
        println(aboutUser.about)
      }

      callMe(requestContext)
    }
  }

}
