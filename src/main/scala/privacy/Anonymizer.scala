package org.example.testsscala
package privacy

trait Anonymizer[-A, +B] extends (A => B)

class ReplacementAnonymizer(replacement: String) extends Anonymizer[Any, String] {

  final override def apply(value: Any): String = replacement

}

object DefaultAnonymizer extends ReplacementAnonymizer("*****")

class StringAnonymizer(min: Int = 0, max: Int = Int.MaxValue, replacement: String = "*") extends Anonymizer[String, String] {

  override def apply(value: String): String = {
    val size = between(min, value.length, max)
    replacement.repeat(size)
  }

  private def between(min: Int, value: Int, max: Int) = math.max(min, math.min(value, max))

}
