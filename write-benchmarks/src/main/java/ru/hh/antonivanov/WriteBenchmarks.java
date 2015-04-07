package ru.hh.antonivanov;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WriteBenchmarks {

  private static final List<String> lines = new ArrayList<>(1000);
  static {
    for(int i=0; i<1000; i++) {
      lines.add("0123456789012345678901234567890123456789012345678901234567890123456789");
    }
  }

  private static final Path diskOutPath = Paths.get("out.txt");
  private static final File diskOutFile = diskOutPath.toFile();
  private static final Path ramOutPath = Paths.get("/dev/shm/out.txt");
  private static final File ramOutFile = ramOutPath.toFile();

  @Benchmark
  public void nioBufferedWriteDisk() throws IOException {
    Files.write(diskOutPath, lines);
  }

  @Benchmark
  public void nioBufferedWriteRam() throws IOException {
    Files.write(ramOutPath, lines);
  }

  @Benchmark
  public void fileBufferedWriteDisk() throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(diskOutFile)))) {
      for (CharSequence line: lines) {
        writer.append(line);
        writer.newLine();
      }
    }
  }

  @Benchmark
  public void fileBufferedWriteRam() throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ramOutFile)))) {
      for (CharSequence line: lines) {
        writer.append(line);
        writer.newLine();
      }
    }
  }

  @Benchmark
  public void fileNonBufferedWriteDisk() throws IOException {
    try (Writer writer = new OutputStreamWriter(new FileOutputStream(diskOutFile))) {
      for (CharSequence line: lines) {
        writer.append(line);
        writer.append('\n');
      }
    }
  }

  @Benchmark
  public void fileNonBufferedWriteRam() throws IOException {
    try (Writer writer = new OutputStreamWriter(new FileOutputStream(ramOutFile))) {
      for (CharSequence line: lines) {
        writer.append(line);
        writer.append('\n');
      }
    }
  }

  public static void main(String[] args) throws RunnerException {
    final Options opt = new OptionsBuilder()
        .include("WriteBenchmarks\\.")
        .forks(1)
        .build();
    new Runner(opt).run();
  }
}
