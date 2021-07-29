package org.example.testsscala
package amount

object Helpers {

  implicit class BigDecimalWithCurrencies(private final val value: BigDecimal) {

    def euros: Amount = Amount(value, EUR)

    def euro: Amount = euros

    def dollars: Amount = Amount(value, USD)

    def dollar: Amount = dollars

  }

  implicit class IntWithCurrencies(value: Int) extends BigDecimalWithCurrencies(value)

  implicit class LongWithCurrencies(value: Long) extends BigDecimalWithCurrencies(value)

  implicit class DoubleWithCurrencies(value: Double) extends BigDecimalWithCurrencies(value)

}
