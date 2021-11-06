package org.example.testsscala
package cats.either

import scala.concurrent.Future

trait Service {

  def compute(id: String): Future[Either[FunctionalError, ServiceResult]]

}
