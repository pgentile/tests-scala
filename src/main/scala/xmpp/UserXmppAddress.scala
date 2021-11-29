package org.example.testsscala
package xmpp

object UserXmppAddress {

  private val pattern = "([^@]+)@([^/]+)".r

  def parse(value: String): Option[XmppAddress] = {
    value match {
      case pattern(local, domain) =>
        Some(UserXmppAddress(local, domain))
      case _ =>
        None
    }
  }

  def apply(local: String, domain: String): XmppAddress = XmppAddress(Some(local), domain, None)

  def validate(address: XmppAddress): Boolean = address.local.nonEmpty && address.resource.isEmpty

  def unapply(address: XmppAddress): Option[(String, String)] = {
    address match {
      case XmppAddress(Some(local), domain, None) => Some(local, domain)
      case _ => None
    }
  }

  def unapply(value: String): Option[(String, String)] = parse(value).flatMap(unapply)

}
