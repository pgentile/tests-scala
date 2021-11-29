package org.example.testsscala

package object privacy {

  type Anonymizer[-A, +B] = (A => B)

}
