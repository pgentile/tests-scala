package org.example.testsscala
package xmpp

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{Inside, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class UserXmppAddressSpec extends AnyFlatSpec with OptionValues with Inside with ScalaCheckPropertyChecks {

  "parse()" should "parse a valid address without resource" in {
    val result = UserXmppAddress.parse("user@example.org")
    inside(result.value) {
      case XmppAddress(local, domain, resource) =>
        local.value shouldEqual "user"
        domain shouldEqual "example.org"
        resource shouldBe empty
    }
  }

  it should "not parse a valid address with resource" in {
    val result = UserXmppAddress.parse("user@example.org/resource")
    result shouldBe empty
  }

  it should "not parse an address without local part" in {
    val result = UserXmppAddress.parse("example.org/resource")
    result shouldBe empty
  }

  it should "not parse an invalid address" in {
    val result = UserXmppAddress.parse("@example.org")
    result shouldBe empty
  }

  "unapply()" should "extract address parts" in {
    val address = UserXmppAddress("local", "example.org")
    inside(address) {
      case UserXmppAddress(local, domain) =>
        local shouldEqual "local"
        domain shouldEqual "example.org"
    }
  }

  it should "not extract address parts from invalid addresses" in {
    implicit val arbitraryInvalidAddress: Arbitrary[XmppAddress] = Arbitrary(Gen.oneOf(
      "local@example.org/resource",
      "example.org/resource",
      "example.org"
    ).map(XmppAddress.parse(_).get))

    forAll { address: XmppAddress =>
      val oUserXmppAddress = UserXmppAddress.unapply(address)
      oUserXmppAddress shouldBe empty
    }
  }

}
