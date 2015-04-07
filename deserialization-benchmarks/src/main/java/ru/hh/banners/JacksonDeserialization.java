package ru.hh.banners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JacksonDeserialization {

  private static final byte[] jsonBytes;
  static {
    try {
      jsonBytes = BenchmarkUtil.jacksonObjectMapper.writeValueAsBytes(BenchmarkUtil.banners);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Benchmark
  public DTOs.Banners objectMapper() throws IOException {
    final InputStream inputStream = new ByteArrayInputStream(jsonBytes);
    return BenchmarkUtil.jacksonObjectMapper.readValue(inputStream, DTOs.Banners.class);
  }

  private static final ObjectReader objectReaderFor = BenchmarkUtil.jacksonObjectMapper.readerFor(DTOs.Banners.class);

  @Benchmark
  public DTOs.Banners readerFor() throws IOException {
    final InputStream inputStream = new ByteArrayInputStream(jsonBytes);
    return objectReaderFor.<DTOs.Banners>readValue(inputStream);
  }

  public static void main(String[] args) throws RunnerException, JAXBException, IOException {
    final Options opt = new OptionsBuilder()
            .include(JacksonDeserialization.class.getSimpleName())
            .forks(3)
            .build();
    new Runner(opt).run();
  }
}
