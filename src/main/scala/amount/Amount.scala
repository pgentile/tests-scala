package org.example.testsscala
package amount

final case class Amount(value: BigDecimal, currency: Currency) {

  def +(other: Amount): Amount = {
    if (currency != other.currency) {
      throw new IllegalArgumentException(s"Different currencies used: $currency vs. ${other.currency}")
    }
    Amount(value + other.value, currency)
  }

  def -(other: Amount): Amount = this + (-other)

  def *(factor: BigDecimal): Amount = Amount(value * factor, currency)

  def unary_+(): Amount = this

  def unary_-(): Amount = Amount(-value, currency)

  override def toString: String = {
    val regex = "(?i)[a-z].*".r
    if (regex.matches(currency.symbol)) {
      s"$value ${currency.symbol}"
    } else {
      s"$value${currency.symbol}"
    }
  }

}

 object Amount {

   def apply(value: BigDecimal, currency: Currency) = new Amount(value, currency)

   def unapply(amount: Amount): Option[(BigDecimal, Currency)] = Some((amount.value, amount.currency))

}
