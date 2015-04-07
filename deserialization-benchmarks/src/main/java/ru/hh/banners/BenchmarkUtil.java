package ru.hh.banners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import org.msgpack.MessagePack;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Collectors.toList;

class BenchmarkUtil {

  static final ObjectMapper jacksonObjectMapper = new ObjectMapper()
          .setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));

  static final JAXBContext jaxbContext;
  static {
    try {
      jaxbContext = JAXBContext.newInstance(DTOs.Banners.class);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  static final JSONJAXBContext jsonJaxbContext;
  static {
    final JSONConfiguration jsonJaxbConfig = JSONConfiguration.natural().build();
    try {
      jsonJaxbContext = new JSONJAXBContext(jsonJaxbConfig, DTOs.Banners.class);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  static final byte[] xmlBytes = xmlBytes();
  private static byte[] xmlBytes() {
    final String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<banners>" +
            "  <banner id=\"46708\" place=\"201\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=46708&amp;token=1451555373.821584323436602&amp;domainAreaId=1\">" +
            "        <vacancy id=\"7997796\"/>" +
            "  </banner>" +
            "  <banner id=\"1586\" place=\"40\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=1586&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"1597\" place=\"33\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=1597&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"205\" place=\"189\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=205&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"204\" place=\"188\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=204&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"203\" place=\"187\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=203&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"202\" place=\"186\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=202&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"201\" place=\"185\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=201&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"7318\" place=\"204\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=7318&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"7319\" place=\"252\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=7319&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"7321\" place=\"282\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=7321&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"7322\" place=\"283\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=7322&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"36574\" place=\"349\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=36574&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "  <banner id=\"36575\" place=\"350\" type=\"html\" width=\"1\" height=\"1\" click=\"https://hhcdn.ru/click?b=36575&amp;token=1451555373.821584323436602&amp;domainAreaId=1\"/>" +
            "</banners>";
    return xmlString.getBytes(StandardCharsets.UTF_8);
  }

  static final DTOs.Banners banners = banners();
  private static DTOs.Banners banners() {
    final Unmarshaller unmarshaller;
    try {
      unmarshaller = jaxbContext.createUnmarshaller();
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }

    final InputStream inputStream = new ByteArrayInputStream(xmlBytes);
    try {
      return (DTOs.Banners) unmarshaller.unmarshal(inputStream);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  static final BannersProtos.Banners protoBanners = protoBanners(banners);
  private static BannersProtos.Banners protoBanners(final DTOs.Banners banners) {
    final List<BannersProtos.Banner> protoBanners = banners.banners.stream().map(banner -> protoBanner(banner)).collect(toList());
    return BannersProtos.Banners.newBuilder().addAllBanners(protoBanners).build();
  }

  private static BannersProtos.Banner protoBanner(final DTOs.Banner xmlBanner) {
    final BannersProtos.Banner.Builder protoBannerBuilder = BannersProtos.Banner.newBuilder()
            .setId(xmlBanner.id)
            .setPlace(xmlBanner.place)
            .setType(xmlBanner.type)
            .setWidth(xmlBanner.width)
            .setHeight(xmlBanner.height)
            .setClick(xmlBanner.click);
    if (xmlBanner.vacancy != null) {
      protoBannerBuilder.setVacancy(protoVacancy(xmlBanner.vacancy));
    }
    return protoBannerBuilder.build();
  }

  private static BannersProtos.Vacancy protoVacancy(final DTOs.Vacancy xmlVacancy) {
    return BannersProtos.Vacancy.newBuilder()
            .setId(xmlVacancy.id)
            .build();
  }

  static final MessagePack messagePack = new MessagePack();
  static {
    messagePack.register(DTOs.Vacancy.class);
    messagePack.register(DTOs.Banner.class);
    messagePack.register(DTOs.Banners.class);
  }

  private BenchmarkUtil() {
  }
}
