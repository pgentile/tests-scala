package org.example.testsscala
package privacy

class ReplacementAnonymizer(replacement: String) extends Anonymizer[Any, String] {

  final override def apply(value: Any): String = replacement

}

object DefaultAnonymizer extends ReplacementAnonymizer("*****")

class StringAnonymizer(min: Int = 0, max: Int = Int.MaxValue, replacement: Char = '*') extends Anonymizer[String, String] {

  override def apply(value: String): String = {
    val size = between(min, value.length, max)
    String.valueOf(replacement).repeat(size)
  }

  private def between(min: Int, value: Int, max: Int) = math.max(min, math.min(value, max))

}
