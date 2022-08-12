package org.example.testsscala
package privacy

import cats.Monad

import scala.annotation.tailrec

package object instances {

  implicit val privacyMonad: Monad[Privacy] = new Monad[Privacy] {

    override def map[A, B](fa: Privacy[A])(f: A => B): Privacy[B] = fa.map(f)

    override def flatMap[A, B](fa: Privacy[A])(f: A => Privacy[B]): Privacy[B] = fa.flatMap(f)

    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Privacy[Either[A, B]]): Privacy[B] = {
      f(a).untaint match {
        case Right(right) => Privacy(right)
        case Left(left) => tailRecM(left)(f)
      }
    }

    override def pure[A](x: A): Privacy[A] = Privacy(x)

  }

  implicit def listAnonymizer[A, B](implicit itemAnonymizer: Anonymizer[A, B]): Anonymizer[Seq[A], String] = Anonymizer { seq =>
    seq.map(item => itemAnonymizer(item)).mkString(", ")
  }

}
