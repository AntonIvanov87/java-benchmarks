package benchmarks;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.charset.Charset;
import java.util.SortedSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@State(Scope.Thread)
public class TestBenchmarks {

  private static final String uri = "/rs/vacancy/short/json";
  private static final String query = "id=18627204&id=15299767&id=16918398&id=13347567&id=18921496&id=17757344&id=19013454&id=18659111&id=17827036&id=17837434&id=19013652&id=19013004&id=18865348&id=16831116&replicaOnlyRq=true";

  @Benchmark
  public String oldOne() {
    return oldOne(uri, query);
  }

  private static String oldOne(String uri, String queryString) {
    SortedSet<String> queryParams = Sets.newTreeSet();
    if (queryString != null) {
      for (NameValuePair param : URLEncodedUtils.parse(queryString, Charset.forName("UTF-8"))) {
        String name = param.getName();
        if ("replicaOnlyRq".equals(name) || "lang".equals(name) || "site".equals(name)) {
          continue;
        }
        queryParams.add(param.getName());
      }
    }

    final String result = CharMatcher.forPredicate(new Predicate<Character>() {
      @Override
      public boolean apply(Character input) {
        return input == null || CharMatcher.inRange('0', '9').matches(input) || CharMatcher.is(',').matches(input);
      }
    }).removeFrom(uri);

    return queryParams.isEmpty() ? result : result + '?' + StringUtils.join(queryParams, "&");
  }

  @Benchmark
  public String newOne() {
    return newOne(uri, query);
  }

  private String newOne(String uri, String queryString) {
    String result = replacePattern.matcher(uri).replaceAll("");
    if (queryString == null) {
      return result;
    }

    String normedNames = URLEncodedUtils.parse(queryString, Charset.forName("UTF-8")).stream()
                             .map(NameValuePair::getName)
                             .filter(name -> !"replicaOnlyRq".equals(name) && !"lang".equals(name) && !"site".equals(name))
                             .sorted()
                             .distinct()
                             .collect(Collectors.joining("&"));

    return normedNames.isEmpty() ? result : result + '?' + normedNames;
  }

  private static final Pattern replacePattern = Pattern.compile("[0-9,]++");

  public static void main(String[] args) throws RunnerException {

    TestBenchmarks benchmarks = new TestBenchmarks();
    if (!benchmarks.newOne().equals(benchmarks.oldOne())) {
      throw new IllegalStateException(benchmarks.newOne() + " != " + benchmarks.oldOne());
    }

    final Options opt = new OptionsBuilder()
                            .include("TestBenchmarks\\.")
                            .forks(1)
                            .warmupIterations(10)
                            .build();
    new Runner(opt).run();
  }
}
