package org.example.testscala.instrumentation.cats;

import kamon.Kamon;
import kamon.context.Context;
import kanela.agent.bootstrap.context.ContextHandler;
import kanela.agent.libs.net.bytebuddy.asm.Advice;

public class CatsSchedulerAdvisor {

  @Advice.OnMethodEnter()
  public static void beforeExecuteCall(@Advice.Argument(value = 0, readOnly = false) Runnable runnable) {
    Context context = Kamon.currentContext();
    System.out.println("[" + Thread.currentThread().getName() + "] Captured some cats call with context " + context);

    runnable = ContextHandler.wrapInContextAware(runnable);
  }
}
