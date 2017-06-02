package ru.hh.antonivanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@State(Scope.Benchmark)
public class RPCBenchmarks {

  private static final UserService userService = new UserService();

  private static final ExecutorService executorService = new ThreadPoolExecutor(
      1, 128, 60, TimeUnit.SECONDS, new SynchronousQueue<>()
  );

  private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

  private static final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
  private static final UserClientOverAsync userClientOverAsync =
      new UserClientOverAsync(asyncHttpClient, executorService, objectMapper);

  private static final CloseableHttpClient apacheHttpClient;
  static {
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
    poolingHttpClientConnectionManager.setDefaultMaxPerRoute(128);
    apacheHttpClient = HttpClients.createMinimal(poolingHttpClientConnectionManager);
  }

  private static final UserClientOverApache userClientOverApache =
      new UserClientOverApache(apacheHttpClient, objectMapper);

  //@Param(value = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})
  @Param(value = {"0"})
  private int sleepMs;

  @Param(value = {"1", "2", "4", "8", "16", "32", "64"})
  private int parallelRequests;

  //@Benchmark
  public User method() throws IOException, InterruptedException {
    return userService.generate(sleepMs);
  }

  @Benchmark
  public User[] asyncHttpClient() throws ExecutionException, InterruptedException {
    return execute(
        () -> userClientOverAsync.getUser(sleepMs)
    );
  }

  @Benchmark
  public User[] apacheHttpClient() throws ExecutionException, InterruptedException {
   return execute(
       () -> executorService.submit(
           () -> userClientOverApache.getUser(sleepMs)
       )
   );
  }

  private User[] execute(Supplier<Future<User>> userFutureSupplier) throws ExecutionException, InterruptedException {
    Future<User>[] futures = new Future[parallelRequests];
    for (int i=0; i<futures.length; i++) {
      futures[i] = userFutureSupplier.get();
    }
    User[] users = new User[futures.length];
    for (int i=0; i<users.length; i++) {
      users[i] = futures[i].get();
    }
    return users;
  }

  @TearDown
  public void tearDown() throws IOException {
    asyncHttpClient.close();
    apacheHttpClient.close();
    executorService.shutdown();
  }

  public static void main(String[] args) throws RunnerException, InterruptedException, ExecutionException {
    RPCBenchmarks rpcBenchmarks = new RPCBenchmarks();
    rpcBenchmarks.sleepMs = 0;
    rpcBenchmarks.parallelRequests = 1;
    User apacheUser = rpcBenchmarks.apacheHttpClient()[0];
    System.out.println("Test. Got user from apache client: " + apacheUser);
    User asyncUser = rpcBenchmarks.asyncHttpClient()[0];
    System.out.println("Test. Got user from async client: " + asyncUser);

    final Options opt = new OptionsBuilder()
                            .include("RPCBenchmarks\\.")
                            .forks(1)
                            .build();
    new Runner(opt).run();
  }
}
