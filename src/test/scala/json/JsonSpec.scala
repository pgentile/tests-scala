package org.example.testsscala
package json

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import play.api.libs.json.{JsValue, Json}

class JsonSpec extends AnyFlatSpec {

  it should "parse JSON" in {
    val email = "test@example.org"
    val data =
      s"""
       {
         "name": "Pierre Gentile",
         "firstName": "Pierre",
         "lastName": "Gentile",
         "contact": {
           "email": "$email",
           "phoneNumber": "+33677889900"
         }
       }""".stripLeading

    val value: JsValue = Json.parse(data)

    val parsedEmail = (value \ "contact" \ "email").as[String]
    parsedEmail should equal(email)
  }

}
