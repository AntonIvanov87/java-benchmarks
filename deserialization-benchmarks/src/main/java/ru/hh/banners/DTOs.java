package ru.hh.banners;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

public class DTOs {
  @XmlRootElement(name = "banners")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Banners {
    @XmlElement(name = "banner")
    public List<Banner> banners = new ArrayList<>();

    @Override
    public boolean equals(Object object) {
      if (this == object) return true;
      if (object == null || getClass() != object.getClass()) return false;

      final Banners thatBanners = (Banners) object;

      return banners.equals(thatBanners.banners);

    }

    @Override
    public int hashCode() {
      return banners.hashCode();
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Banner {
    @XmlAttribute(required = true)
    public Integer id;
    @XmlAttribute(required = true)
    public Integer place;
    @XmlAttribute(required = true)
    public String type;
    @XmlAttribute
    public Integer width;
    @XmlAttribute
    public Integer height;
    @XmlAttribute
    public String click;

    @XmlElement
    public Vacancy vacancy;

    @Override
    public boolean equals(Object object) {
      if (this == object) return true;
      if (object == null || getClass() != object.getClass()) return false;

      final Banner banner = (Banner) object;

      if (!id.equals(banner.id)) return false;
      if (!place.equals(banner.place)) return false;
      if (!type.equals(banner.type)) return false;
      if (width != null ? !width.equals(banner.width) : banner.width != null) return false;
      if (height != null ? !height.equals(banner.height) : banner.height != null) return false;
      if (click != null ? !click.equals(banner.click) : banner.click != null) return false;
      return vacancy != null ? vacancy.equals(banner.vacancy) : banner.vacancy == null;

    }

    @Override
    public int hashCode() {
      int result = id.hashCode();
      result = 31 * result + place.hashCode();
      result = 31 * result + type.hashCode();
      result = 31 * result + (width != null ? width.hashCode() : 0);
      result = 31 * result + (height != null ? height.hashCode() : 0);
      result = 31 * result + (click != null ? click.hashCode() : 0);
      result = 31 * result + (vacancy != null ? vacancy.hashCode() : 0);
      return result;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement
  public static class Vacancy {
    @XmlAttribute(required = true)
    public Integer id;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Vacancy)) return false;

      Vacancy vacancy = (Vacancy) o;

      return id.equals(vacancy.id);

    }

    @Override
    public int hashCode() {
      return id.hashCode();
    }
  }

  private DTOs() {
  }
}
