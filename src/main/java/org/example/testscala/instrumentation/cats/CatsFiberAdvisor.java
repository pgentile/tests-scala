package org.example.testscala.instrumentation.cats;

import cats.effect.IO;
import kamon.Kamon;
import kamon.context.Context;
import kanela.agent.libs.net.bytebuddy.asm.Advice;
import scala.Function0;
import scala.jdk.FunctionWrappers;

import java.util.function.Function;

public class CatsFiberAdvisor {

  @Advice.OnMethodExit
  public static void beforeFiberConstruction(
    @Advice.This Object self,
    @Advice.FieldValue(value = "resumeIO", readOnly = false) Object resumeIO
  ) {
    Context context = Kamon.currentContext();
    System.out.println("[" + Thread.currentThread().getName() + "] Fiber call with context " + context + " (fiber = " + self + ")");
    // new RuntimeException().printStackTrace(System.out);

    final var copy = (IO<?>) resumeIO;
    // resumeIO = copy;
    // resumeIO = wrapIO(copy);

    resumeIO = IO
      .apply(new StoreContextF(context))
      .flatMap(new FunctionWrappers.FromJavaFunction(new RunIO(copy)));
  }

  public static IO<?> wrapIO(IO<?> io) {
    return IO.pure("Example");
  }

  public static class StoreContextF implements Function0<Object> {

    private Context context;

    public StoreContextF(Context context) {
      this.context = context;
    }

    @Override
    public Object apply() {
      return Kamon.storeContext(context);
    }

  }

  public static class RunIO implements Function<Object, IO<?>> {

    private IO<?> io;

    public RunIO(IO<?> io) {
      this.io = io;
    }

    @Override
    public IO<?> apply(Object o) {
      return io;
    }
  }

}
