import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

class Main {

  static final int numOfWarmUps = 2;
  static final int numOfTests = 5;
  static final int numOfTasks = 16_384;
  static final int numOfIterationsPerTask = 600_000;

  public static void main(String[] args) throws Exception {
    for (int numOfThreads : new int[] {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192}) {
      System.out.println(numOfThreads + " threads.");
      ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

      System.out.println("Warming up...");
      for (int i=0; i < numOfWarmUps; i++) {
        test(executorService);
      }

      System.out.println("Testing...");
      for (int i = 0; i < numOfTests; i++) {
        long start = currentTimeMillis();
        test(executorService);
        System.out.println(currentTimeMillis() - start);
      }

      executorService.shutdown();
      executorService.awaitTermination(1, TimeUnit.SECONDS);
      System.out.println();
    }
  }

  static void test(ExecutorService executorService) throws Exception {
//    Task.resetSimultaneous();
    List<Future<Integer>> resultsFutures = new ArrayList<>(numOfTasks);
    for (int i = 0; i < numOfTasks; i++) {
      resultsFutures.add(executorService.submit(new Task()));
    }
    for (Future<Integer> resultFuture : resultsFutures) {
      resultFuture.get();
    }
//    System.out.println("Max simultaneous: " + Task.maxSimultaneous);
  }

  static class Task implements Callable<Integer> {
//    private static int currentSimultaneous = 0;
//    static int maxSimultaneous = 0;
//    private static final Object simultaneousMonitor = new Object();

//    private static void resetSimultaneous() {
//      synchronized (simultaneousMonitor) {
//        currentSimultaneous = 0;
//        maxSimultaneous = 0;
//      }
//    }

//    private static void incSimultaneous() {
//      synchronized (simultaneousMonitor) {
//        currentSimultaneous++;
//        if (currentSimultaneous > maxSimultaneous) {
//          maxSimultaneous = currentSimultaneous;
//        }
//      }
//    }

//    private static void decSimultaneous() {
//      synchronized (simultaneousMonitor) {
//        currentSimultaneous--;
//      }
//    }

    private final Random random = new Random();
    @Override
    public Integer call() throws InterruptedException {
//      incSimultaneous();
      int sum = 0;
      for (int i = 0; i < numOfIterationsPerTask; i++) {
        sum += random.nextInt();
      }
//      decSimultaneous();
      return sum;
    }
  }
}
