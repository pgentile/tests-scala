package org.example.testsscala
package complex

import scala.math.{Pi, atan, signum, sqrt}

case class Complex(re: Double, im: Double) {

  def unary_-(): Complex = Complex(-re, -im)

  def +(other: Complex): Complex = Complex(re + other.re, im + other.im)

  def -(other: Complex): Complex = Complex(re - other.re, im - other.im)

  def *(other: Complex): Complex = {
    // (a + bi) * (c + di) = (a * c) + (a * di) + (bi * c) + (bi * di)
    // (a + bi) * (c + di) = (a * c) + (a * di) + (bi * c) + (-1 * b * d)
    // (a + bi) * (c + di) = (a * c) - (b * d) + (a * d * i) + (b * c * i)
    Complex((re * other.re) - (im * other.im), (re * other.im) + (im * other.re))
  }

  def /(other: Complex): Complex = {
    // ((aA + bB) + i * (bA - aB)) / (A^2 + B^2)
    Complex(
      (re * other.re + im * other.im) / (other.re * other.re + other.im * other.im),
      (im * other.re - re * other.im) / (other.re * other.re + other.im * other.im)
    )
  }

  def phi: Double = sqrt(re * re + im * im)

  def theta: Double = {
    if (re == 0 && im == 0) 0
    else if (re == 0) signum(im) * Pi / 2
    else atan(im / re)
  }

  override def toString: String = {
    if (im == 0) re.toString
    else if (re == 0) s"${im}i"
    else s"$re + ${im}i"
  }

}
