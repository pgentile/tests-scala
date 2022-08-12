package org.example.testsscala
package instrumentation

import kamon.Kamon
import kamon.instrumentation.context.HasContext
import kanela.agent.api.instrumentation.InstrumentationBuilder
import kanela.agent.libs.net.bytebuddy.asm.Advice

class CatsInstrumentation extends InstrumentationBuilder {

  println(s"Called instrumentation: ${getClass}")

  onType("cats.effect.IOFiber")
    .mixin(classOf[HasContext.Mixin])
    .advise(method("run"), classOf[CatsFiberRunAdvisor])
    .advise(method("scheduleFiber").or(method("rescheduleFiber")), classOf[CatsFiberScheduleAdvisor])

}

class CatsFiberRunAdvisor
object CatsFiberRunAdvisor {

  @Advice.OnMethodEnter
  def beforeRunCall(@Advice.This fiberWithContext: HasContext): Unit = {
    val context = fiberWithContext.context
    Kamon.storeContext(context)
    // println(s"[${Thread.currentThread} -- $fiberWithContext -- Run] Before - Set current context to $context from fiber")
    // println(s"Running fiber $fiberWithContext")
  }

  @Advice.OnMethodExit
  def afterRunCall(@Advice.This fiberWithContext: HasContext): Unit = {
    val context = Kamon.currentContext()
    fiberWithContext.setContext(context)
    // println(s"[${Thread.currentThread} -- $fiberWithContext -- Run] After - Saving context $context in fiber")
  }

}

class CatsFiberScheduleAdvisor
object CatsFiberScheduleAdvisor {

  @Advice.OnMethodEnter
  def beforeScheduleCall(@Advice.This fiberWithContext: HasContext, @Advice.Argument(1) fiberWithContextArg: HasContext): Unit = {
    // val context = fiberWithContext.context
    // Kamon.storeContext(context)
    // println(s"[${Thread.currentThread} -- $fiberWithContext -- Schedule] Before")
    // println(s"Running fiber $fiberWithContext")

    if (fiberWithContext ne fiberWithContextArg) {
      // println(s"[${Thread.currentThread} -- $fiberWithContext -- Schedule] Before -- Different fibers, coping current Kamon context to the new Fiber")
      fiberWithContextArg.setContext(Kamon.currentContext())
    }
  }

  @Advice.OnMethodExit
  def afterScheduleCall(@Advice.This fiberWithContext: HasContext, @Advice.Argument(1) fiberWithContextArg: HasContext): Unit = {
    // val context = Kamon.currentContext()
    // fiberWithContext.setContext(context)
    // println(s"[${Thread.currentThread} -- $fiberWithContext -- Schedule] After")
  }

}

