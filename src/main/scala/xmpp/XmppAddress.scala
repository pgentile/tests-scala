package org.example.testsscala
package xmpp

case class XmppAddress(
  local: Option[String],
  domain: String,
  resource: Option[String]
) {

  def withLocal(local: String): XmppAddress = copy(local = Some(local))

  def withoutLocal: XmppAddress = copy(local = None)

  def withResource(resource: String): XmppAddress = copy(resource = Some(resource))

  def withoutResource: XmppAddress = copy(resource = None)

  def withDomain(domain: String): XmppAddress = copy(domain = domain)

  override def toString: String = {
    Seq(
      local.map(_ + "@").getOrElse(""),
      domain,
      resource.map("/" + _).getOrElse("")
    ).mkString
  }

}

object XmppAddress {

  private val pattern = "(?:([^@]+)@)?([^/]+)(?:/(.+))?".r

  def parse(value: String): Option[XmppAddress] = {
    value match {
      case pattern(local, domain, resource) =>
        Some(XmppAddress(Option(local), domain, Option(resource)))
      case _ =>
        None
    }
  }

  def unapply(value: String): Option[(Option[String], String, Option[String])] = parse(value).flatMap(unapply)

}
