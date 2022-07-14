package org.example.testscala.instrumentation.cats;

import kamon.Kamon;
import kamon.context.Context;
import kamon.context.Storage;
import kanela.agent.api.instrumentation.InstrumentationBuilder;
import kanela.agent.bootstrap.context.ContextHandler;
import kanela.agent.bootstrap.context.ContextProvider;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.Callable;

public class CatsInstrumentation extends InstrumentationBuilder {

  public CatsInstrumentation() {
    System.out.println("Cats instrumented");

    ContextHandler.setContextProvider(new KamonContextProvider());

    //    onSubTypesOf("scala.concurrent.ExecutionContext").advise(
    //      method("execute").and(withArgument(Runnable.class)),
    //      CatsExecutionContextAdvisor.class
    //    );

    //    onSubTypesOf("cats.effect.unsafe.Scheduler").advise(
    //      method("sleep").and(withArgument(0, FiniteDuration.class)).and(withArgument(1, Runnable.class)),
    //      CatsSchedulerAdvisor.class
    //    );

    onSubTypesOf("cats.effect.IOFiber").advise(
      isConstructor().and(takesArguments(5)),
      CatsFiberAdvisor.class
    );
  }

  /**
   * implementation of kanela.agent.bootstrap.context.ContextProvider
   */
  private static class KamonContextProvider implements ContextProvider {
    @Override
    public Runnable wrapInContextAware(Runnable runnable) {
      return new ContextAwareRunnable(runnable);
    }

    @Override
    public <A> Callable<?> wrapInContextAware(Callable<A> callable) {
      return new ContextAwareCallable<>(callable);
    }
  }


  /**
   * Runs a Runnable within Kamon Context
   */
  private static class ContextAwareRunnable implements Runnable {

    private final Runnable underlying;
    private final Context context;

    ContextAwareRunnable(Runnable r) {
      this.context = Kamon.currentContext();
      this.underlying = r;

      System.out.println("[" + Thread.currentThread().getName() + "] Wrapping with " + context + ": " + underlying);
    }

    @Override
    public void run() {
      try (Storage.Scope ignored = Kamon.storeContext(context)) {
        System.out.println("[" + Thread.currentThread().getName() + "] Execution with " + context + ": " + underlying);
        underlying.run();
      }
    }
  }

  /**
   * Runs a Callable within Kamon Context
   */
  private static class ContextAwareCallable<A> implements Callable<A> {

    private final Callable<A> underlying;
    private final Context context;

    ContextAwareCallable(Callable<A> c) {
      this.context = Kamon.currentContext();
      this.underlying = c;

      System.out.println("[" + Thread.currentThread().getName() + "] Wrapping with " + context + ": " + underlying);
    }

    public A call() throws Exception {
      try (Storage.Scope ignored = Kamon.storeContext(context)) {
        System.out.println("[" + Thread.currentThread().getName() + "] Execution with " + context + ": " + underlying);
        return underlying.call();
      }
    }
  }
}
