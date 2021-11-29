package org.example.testsscala
package xmpp

import org.scalatest.{Inside, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

class XmppAddressSpec extends AnyFlatSpec with OptionValues with Inside {

  "parse()" should "parse address with local part and resource" in {
    val oAddress = XmppAddress.parse("local@example.org/resource")
    inside(oAddress.value) {
      case XmppAddress(local, domain, resource) =>
        local.value shouldEqual "local"
        domain shouldEqual "example.org"
        resource.value shouldEqual "resource"
    }
  }

  it should "parse address with local part and no resource" in {
    val oAddress = XmppAddress.parse("local@example.org")
    inside(oAddress.value) {
      case XmppAddress(local, domain, resource) =>
        local.value shouldEqual "local"
        domain shouldEqual "example.org"
        resource shouldBe empty
    }
  }

  it should "parse address with no local part and resource" in {
    val oAddress = XmppAddress.parse("example.org/resource")
    inside(oAddress.value) {
      case XmppAddress(local, domain, resource) =>
        local shouldBe empty
        domain shouldEqual "example.org"
        resource.value shouldEqual "resource"
    }
  }

  it should "parse address with only domain" in {
    val oAddress = XmppAddress.parse("example.org")
    inside(oAddress.value) {
      case XmppAddress(local, domain, resource) =>
        local shouldBe empty
        domain shouldEqual "example.org"
        resource shouldBe empty
    }
  }

  it should "not parse invalid address" in {
    val oAddress = XmppAddress.parse("@example.org/")
    oAddress shouldBe empty
  }

  "unapply()" should "extract address parts" in {
    val oAddress = XmppAddress.parse("local@example.org/resource")
    inside(oAddress.value) {
      case XmppAddress(local, domain, resource) =>
        local.value shouldEqual "local"
        domain shouldEqual "example.org"
        resource.value shouldEqual "resource"
    }
  }

  it should "extract address parts without resource" in {
    val oAddress = XmppAddress.parse("local@example.org")
    inside(oAddress.value) {
      case XmppAddress(local, domain, resource) =>
        local.value shouldEqual "local"
        domain shouldEqual "example.org"
        resource shouldBe empty
    }
  }

  it should "extract address parts without local part and without resource" in {
    val oAddress = XmppAddress.parse("example.org")
    inside(oAddress.value) {
      case XmppAddress(local, domain, resource) =>
        local shouldBe empty
        domain shouldEqual "example.org"
        resource shouldBe empty
    }
  }
}
