package ru.hh.banners;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Deserialization {

  static {
    //System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
    System.setProperty("javax.xml.parsers.SAXParserFactory", "com.ctc.wstx.sax.WstxSAXParserFactory");
  }

  @Benchmark
  public DTOs.Banners xml() throws JAXBException, IOException {
    final InputStream inputStream = new ByteArrayInputStream(BenchmarkUtil.xmlBytes);
    return (DTOs.Banners) BenchmarkUtil.jaxbContext.createUnmarshaller().unmarshal(inputStream);
  }

  private static final byte[] protoBytes = BenchmarkUtil.protoBanners.toByteArray();

  @Benchmark
  public BannersProtos.Banners protobuf() throws IOException {
    final InputStream inputStream = new ByteArrayInputStream(protoBytes);
    return BannersProtos.Banners.parseFrom(inputStream);
  }

  private static final byte[] jsonBytes;
  static {
    try {
      jsonBytes = BenchmarkUtil.jacksonObjectMapper.writeValueAsBytes(BenchmarkUtil.banners);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Benchmark
  public DTOs.Banners jsonJackson() throws IOException {
    final InputStream inputStream = new ByteArrayInputStream(jsonBytes);
    return BenchmarkUtil.jacksonObjectMapper.readValue(inputStream, DTOs.Banners.class);
  }

  //@Benchmark // for some reason does not deserialize banner type field
  public DTOs.Banners jsonJerseyJaxb() throws JAXBException {
    final InputStream inputStream = new ByteArrayInputStream(jsonBytes);
    return BenchmarkUtil.jsonJaxbContext.createJSONUnmarshaller().unmarshalFromJSON(inputStream, DTOs.Banners.class);
  }

  private static final byte[] msgPackBytes;
  static {
    try {
      msgPackBytes = BenchmarkUtil.messagePack.write(BenchmarkUtil.banners);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Benchmark
  public DTOs.Banners msgpack() throws IOException {
    final InputStream inputStream = new ByteArrayInputStream(msgPackBytes);
    return BenchmarkUtil.messagePack.read(inputStream, DTOs.Banners.class);
  }

  public static void main(String[] args) throws RunnerException, JAXBException, IOException {
    final Deserialization benchmarks = new Deserialization();
    final DTOs.Banners xml = benchmarks.xml();

    if (!benchmarks.jsonJackson().equals(xml)) {
      throw new IllegalStateException("jsonJackson != xml");
    }

    // for some reason does not deserialize banner type field
    //if (!benchmarks.jsonJerseyJaxb().equals(xml)) {
    //  throw new IllegalStateException("jsonJerseyJaxb != xml");
    //}

    if (!benchmarks.msgpack().equals(xml)) {
      throw new IllegalStateException("msgpack != xml");
    }

    final Options opt = new OptionsBuilder()
            .include("(Deserialization\\.xml)|(Deserialization\\.jsonJackson)")
            .jvmArgs("-Xmx256M", "-XX:+UseG1GC", "-Xss256K")
            .threads(4)
            .forks(1)
            .build();
    new Runner(opt).run();
  }
}
