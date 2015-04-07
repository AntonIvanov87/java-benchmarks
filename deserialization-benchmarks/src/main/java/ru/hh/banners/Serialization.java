package ru.hh.banners;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Serialization {

  //@Benchmark
  public ByteArrayOutputStream xml() throws JAXBException, IOException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BenchmarkUtil.jaxbContext.createMarshaller().marshal(BenchmarkUtil.banners, outputStream);
    return outputStream;
  }

  //@Benchmark
  public ByteArrayOutputStream protobuf() throws IOException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BenchmarkUtil.protoBanners.writeTo(outputStream);
    return outputStream;
  }

  @Benchmark
  public ByteArrayOutputStream jsonJackson() throws IOException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BenchmarkUtil.jacksonObjectMapper.writeValue(outputStream, BenchmarkUtil.banners);
    return outputStream;
  }

  //@Benchmark
  public ByteArrayOutputStream jsonJerseyJaxb() throws JAXBException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BenchmarkUtil.jsonJaxbContext.createJSONMarshaller().marshallToJSON(BenchmarkUtil.banners, outputStream);
    return outputStream;
  }

  @Benchmark
  public ByteArrayOutputStream msgpack() throws IOException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BenchmarkUtil.messagePack.write(outputStream, BenchmarkUtil.banners);
    return outputStream;
  }

  public static void main(String[] args) throws RunnerException, JAXBException, IOException {
    Serialization benchmarks = new Serialization();

    byte[] xmlBytes = benchmarks.xml().toByteArray();
    DTOs.Banners bannersFromXML = (DTOs.Banners) BenchmarkUtil.jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(xmlBytes));
    assert bannersFromXML.equals(BenchmarkUtil.banners);

    byte[] jsonBytes = benchmarks.jsonJackson().toByteArray();
    DTOs.Banners bannersFromJSON = BenchmarkUtil.jacksonObjectMapper.readValue(jsonBytes, DTOs.Banners.class);
    assert bannersFromJSON.equals(BenchmarkUtil.banners);

    byte[] msgPackBytes = benchmarks.msgpack().toByteArray();
    DTOs.Banners bannersFromMsgPack = BenchmarkUtil.messagePack.read(msgPackBytes, DTOs.Banners.class);
    assert bannersFromMsgPack.equals(BenchmarkUtil.banners);

    final Options opt = new OptionsBuilder()
            .include("\\.Serialization\\.")
            .forks(3)
            .build();
    new Runner(opt).run();
  }
}
