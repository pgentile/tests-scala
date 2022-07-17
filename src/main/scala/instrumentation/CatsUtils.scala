package org.example.testsscala
package instrumentation

import cats.effect.IOLocal
import kamon.context.Context

object CatsUtils {

  def getKamonContextFromState(state: Map[IOLocal[_], Any]): Context = {
    state.values
      .collectFirst {
        case ctx: Context => ctx
      }
      .getOrElse(Context.Empty)
  }

}
