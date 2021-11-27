package org.example.testsscala
package fp.traverse

import scala.concurrent.Future

object CatsTraverseApp extends App {

  val of1 = Some(Future.successful("OK"))
  val of2: Option[List[String]] = None

}
