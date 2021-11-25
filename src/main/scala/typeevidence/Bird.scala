package org.example.testsscala
package typeevidence

trait Bird {
  def name: String = s"Bird ${getClass.getSimpleName.toLowerCase}"
}

class Parrot extends Bird

class Cockatoo extends Parrot

class Canary extends Bird
