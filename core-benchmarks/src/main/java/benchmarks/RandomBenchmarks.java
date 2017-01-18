package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class RandomBenchmarks {

  private static final Random random = new Random();
  private static final AtomicLong atomicLong = new AtomicLong();

  @Benchmark
  public int random() {
    return random.nextInt();
  }

  @Benchmark
  public int threadLocalRandomCurrent() {
    return ThreadLocalRandom.current().nextInt();
  }

  @Benchmark
  public long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  @Benchmark
  public long nanoTime() {
    return System.nanoTime();
  }

  @Benchmark
  public long atomicLong() {
    return atomicLong.incrementAndGet();
  }

  public static void main(String[] args) throws RunnerException {
    final Options opt = new OptionsBuilder()
                            .include("RandomBenchmarks\\.atomicLong")
                            .forks(1)
                            .timeUnit(TimeUnit.MILLISECONDS)
                            .build();
    new Runner(opt).run();
  }
}
