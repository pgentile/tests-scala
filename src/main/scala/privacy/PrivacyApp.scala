package org.example.testsscala
package privacy

import java.time.LocalDate

import cats.syntax.traverse._
import instances._

object PrivacyApp extends App {

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

  val list = Seq("Jean", "Bon", "Guy", "Gnol").map(Privacy(_))
  val privacySeq: Privacy[Seq[String]] = list.sequence

  implicit val itemAnonymizer: Anonymizer[String, String] = ReplacementAnonymizer("#####")

  println(s"list = ${privacySeq.anonymize}")

  private def toS(obj: AnyRef): String = obj.toString

  private def flattenPrivacy[A](seq: Seq[Privacy[A]]): Privacy[Seq[A]] = {
    seq.foldLeft(Privacy(Seq.empty[A])) { (accumulator, privacy) =>
      for (list <- accumulator; value <- privacy) yield list :+ value
    }
  }

  private object ListAnonymizer extends Anonymizer[Seq[Any], String] {
    override def apply(value: Seq[Any]): String = value.map(item => DefaultAnonymizer(item)).mkString(", ")
  }

}
