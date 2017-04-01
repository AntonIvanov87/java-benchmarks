package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@State(Scope.Benchmark)
public class AtomicBenchmarks {

  @State(Scope.Benchmark)
  public static class AtomicIntegerCounter {
    private final AtomicInteger atomicInteger = new AtomicInteger();

    int get() {
      return atomicInteger.get();
    }

    boolean compareAndSet(int expect, int update) {
      return atomicInteger.compareAndSet(expect, update);
    }
  }

  @State(Scope.Benchmark)
  public static class FieldUpdaterCounter {
    private static final AtomicIntegerFieldUpdater<FieldUpdaterCounter> fieldUpdater =
        AtomicIntegerFieldUpdater.newUpdater(FieldUpdaterCounter.class, "field");

    private volatile int field = 0;

    int get() {
      return fieldUpdater.get(this);
    }

    boolean compareAndSet(int expect, int update) {
      return fieldUpdater.compareAndSet(this, expect, update);
    }
  }

  private int random;

  @Setup(Level.Invocation)
  public void setUpInvocation() {
    random = ThreadLocalRandom.current().nextInt();
  }

  @Benchmark
  public boolean atomicIntegerGetCompareAndSet(AtomicIntegerCounter counter) {
    int current = counter.get();
    return counter.compareAndSet(current, random);
  }

  @Benchmark
  public boolean fieldUpdaterGetCompareAndSet(FieldUpdaterCounter counter) {
    int current = counter.get();
    return counter.compareAndSet(current, random);
  }

  public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
        .include("AtomicBenchmarks\\.")
        .threads(2)
        .forks(1)
        .build();

    new Runner(options).run();
  }


}
