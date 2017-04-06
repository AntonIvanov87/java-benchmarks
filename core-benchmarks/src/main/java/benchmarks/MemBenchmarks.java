package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class MemBenchmarks {

  @Param({"4096", "2048", "1024", "512", "256", "128", "64", "32", "16", "8", "4", "2", "1"})
  int targetSizeMb;
  int arrSize;
  int[] arr;

  @Setup(Level.Trial)
  public void setUpTrial() {
    arrSize = (int) (targetSizeMb * 1024L * 1024 / 4);
    arr = new int[arrSize];
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i=0; i<arrSize; i++) {
      arr[i] = random.nextInt();
    }
  }

  private int index;

  @Setup(Level.Invocation)
  public void setUpInvocation() {
    index = ThreadLocalRandom.current().nextInt(arrSize);
  }

  @Benchmark
  public int cell() {
    return arr[index];
  }

  public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
                          .include("MemBenchmarks\\.")
                          .threads(2)
                          .forks(1)
                          .mode(Mode.SampleTime)
                          .timeUnit(TimeUnit.NANOSECONDS)
                          .build();

    new Runner(options).run();
  }


}
