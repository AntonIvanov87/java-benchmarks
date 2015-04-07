package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class ExceptionBenchmarks {

  private static final Exception exception = new Exception("foo");

  @Benchmark
  public Object returnInteger() {
    return new Integer(42);
  }

  @Benchmark
  public Object createException() {
    return new Exception("foo");
  }

  @Benchmark
  public Object throwCatchStaticException() {
    try {
      throw exception;
    } catch (Exception e) {
      return e;
    }
  }

  @Benchmark
  public Object throwCatchStaticExceptionUseStack() {
    try {
      throw exception;
    } catch (Exception e) {
      return e.getStackTrace();
    }
  }

  @Benchmark
  public Object throwCatchException() {
    try {
      throw new Exception("foo");
    } catch (Exception e) {
      return e;
    }
  }

  @Benchmark
  public Object throwCatchExceptionUseStack() {
    try {
      throw new Exception("foo");
    } catch (Exception e) {
      return e.getStackTrace();
    }
  }

  public static void main(String[] args) throws RunnerException {
    final Options opt = new OptionsBuilder()
                            .include("ExceptionBenchmarks\\.")
                            .mode(Mode.AverageTime)
                            .timeUnit(TimeUnit.NANOSECONDS)
                            .forks(1)
                            .build();
    new Runner(opt).run();
  }
}
