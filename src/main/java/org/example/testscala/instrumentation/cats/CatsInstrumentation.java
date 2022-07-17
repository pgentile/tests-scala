package org.example.testscala.instrumentation.cats;

import kanela.agent.api.instrumentation.InstrumentationBuilder;

public class CatsInstrumentation extends InstrumentationBuilder {

  public CatsInstrumentation() {
    //    onSubTypesOf("scala.concurrent.ExecutionContext").advise(
    //      method("execute").and(withArgument(Runnable.class)),
    //      CatsExecutionContextAdvisor.class
    //    );

    //    onSubTypesOf("cats.effect.unsafe.Scheduler").advise(
    //      method("sleep").and(withArgument(0, FiniteDuration.class)).and(withArgument(1, Runnable.class)),
    //      CatsSchedulerAdvisor.class
    //    );

    onType("cats.effect.IOFiber")
      .advise(
        method("runLoop").or(method("succeeded")).or(method("failed")),
        CatsFiberAdvisor.class
      );
  }

}
