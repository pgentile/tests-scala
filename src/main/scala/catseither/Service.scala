package org.example.testsscala
package catseither

import scala.concurrent.Future

trait Service {

  def compute(id: String): Future[Either[FunctionalError, ServiceResult]]

}
