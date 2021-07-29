package org.example.testsscala
package privacy

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

class PrivacySpec extends AnyFlatSpec {

  private case class ValueToProtect(value: String)

  "Privacy for equal objects" should "be equal" in {
    val p1 = Privacy(ValueToProtect("A"))
    val p2 = Privacy(ValueToProtect("A"))

    p1 should equal(p2)
    p1.hashCode() should equal(p2.hashCode())
  }

  "Privacy for non equal objects" should "not be equal" in {
    val p1 = Privacy(ValueToProtect("A"))
    val p2 = Privacy(ValueToProtect("B"))

    p1 shouldNot equal(p2)
  }

  "map a Privacy object" should "map value" in {
    val p = Privacy("A").map(value => value + value)
    p.untaint should equal("AA")
  }

}
