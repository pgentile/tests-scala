package org.example.testsscala
package workwithimplicits

class AboutUser {

  def about(implicit requestContext: RequestContext): String = {
    requestContext.username.map(name => s"Current user is ${name.value}").getOrElse("Unknown user")
  }

}
