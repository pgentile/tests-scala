package org.example.testsscala
package privacy

import java.time.{LocalDate, YearMonth}

object PrivacyApp extends App {

  private def toS(obj: AnyRef): String = obj.toString

  val p1 = Privacy("ABC")
  val p2 = Privacy(1)

  println(p1)
  println(p2)

  println(p1.map(toS))

  val result = for (value <- p1) yield value
  println(result)

  val merged = for (v1 <- p1; v2 <- p2) yield s"$v1 / $v2"
  println(merged)

  val birthDateAnonymizer: Anonymizer[LocalDate, String] = date => f"${date.getYear}%04d-${date.getMonthValue}%02d-**"
  val birthDate = Privacy(LocalDate.of(1996, 7, 10), birthDateAnonymizer)
  println(birthDate)

}
