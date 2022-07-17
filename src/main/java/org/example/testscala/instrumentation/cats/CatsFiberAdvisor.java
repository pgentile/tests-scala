package org.example.testscala.instrumentation.cats;

import cats.effect.IOLocal;
import kamon.Kamon;
import kamon.context.Context;
import kanela.agent.libs.net.bytebuddy.asm.Advice;
import org.example.testsscala.instrumentation.CatsUtils;

public class CatsFiberAdvisor {

  @Advice.OnMethodEnter
  public static void beforeFiberRun(
    @Advice.This Object self,
    @Advice.FieldValue(value = "localState") scala.collection.immutable.Map<IOLocal<?>, Object> localState
  ) {
    Context context = CatsUtils.getKamonContextFromState(localState);
    Kamon.storeContext(context);

    // System.out.println("[" + Thread.currentThread().getName() + "] Fiber state (fiber = " + self + ", localState = " + localState + ")");
  }

  @Advice.OnMethodExit
  public static void afterFiberRun(
    @Advice.This Object self,
    @Advice.FieldValue(value = "localState") scala.collection.immutable.Map<IOLocal<?>, Object> localState
  ) {
    // Kamon.storeContext(Context.Empty());
  }

}
