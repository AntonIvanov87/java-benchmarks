package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.regex.Pattern;

@State(Scope.Thread)
public class StringReplaceBenchmarks {

  private static final Pattern dotPattern = Pattern.compile("\\.");

  private String string;

  @Setup
  public void prepare() {
    string = "192.168.0.1";
  }

  @Benchmark
  public String stringReplace() {
    return string.replace('.', '-');
  }

  @Benchmark
  public String charArrayReplace() {
    char[] chars = string.toCharArray();
    for (int i=0; i<chars.length; i++) {
      if (chars[i] == '.') {
        chars[i] = '-';
      }
    }
    return new String(chars);
  }

  @Benchmark
  public String stringReplaceAll() {
    return string.replaceAll("\\.", "-");
  }

  @Benchmark
  public String patternReplaceAll() {
    return dotPattern.matcher(string).replaceAll("-");
  }

  public static void main(String[] args) throws RunnerException {

    StringReplaceBenchmarks benchmarks = new StringReplaceBenchmarks();
    benchmarks.prepare();
    if (!benchmarks.stringReplaceAll().equals(benchmarks.patternReplaceAll())) {
      throw new IllegalStateException("stringReplaceAll != patternReplaceAll");
    }
    if (!benchmarks.stringReplaceAll().equals(benchmarks.stringReplace())) {
      throw new IllegalStateException("stringReplaceAll != stringReplace");
    }
    if (!benchmarks.stringReplaceAll().equals(benchmarks.charArrayReplace())) {
      throw new IllegalStateException("stringReplaceAll != charArrayReplace");
    }

    final Options opt = new OptionsBuilder()
                            .include("StringReplaceBenchmarks\\.")
                            .forks(1)
                            .build();
    new Runner(opt).run();
  }
}
