package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class ThreadsBenchmarks {

  private static void work() {
    Blackhole.consumeCPU(500_000);
    try {
      Thread.sleep(1L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    Blackhole.consumeCPU(500_000);
  }

  @Benchmark
  @Threads(1)
  public void thread1() {
    work();
  }

  @Benchmark
  @Threads(2)
  public void thread2() {
    work();
  }

  @Benchmark
  @Threads(4)
  public void thread4() {
    work();
  }

  @Benchmark
  @Threads(8)
  public void thread8() {
    work();
  }

  @Benchmark
  @Threads(16)
  public void thread16() {
    work();
  }

  @Benchmark
  @Threads(32)
  public void thread32() {
    work();
  }

  @Benchmark
  @Threads(64)
  public void thread64() {
    work();
  }

  @Benchmark
  @Threads(128)
  public void thread128() {
    work();
  }

  @Benchmark
  @Threads(256)
  public void thread256() {
    work();
  }

  @Benchmark
  @Threads(384)
  public void thread384() {
    work();
  }

  @Benchmark
  @Threads(416)
  public void thread416() {
    work();
  }

  @Benchmark
  @Threads(448)
  public void thread448() {
    work();
  }

  @Benchmark
  @Threads(512)
  public void thread512() {
    work();
  }

  @Benchmark
  @Threads(1024)
  public void thread1024() {
    work();
  }

  @Benchmark
  @Threads(2048)
  public void thread2048() {
    work();
  }

  public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
                          //.include("ThreadsBenchmarks\\.thread[2-5][0-9][0-9]$")
                          .include("ThreadsBenchmarks\\.thread2048$")
                          .forks(1)
                          .build();

    new Runner(options).run();
  }
}
