package org.ujar.bs.dst.k8s.bookingdb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ujar.bs.dst.k8s.bookingdb.persistence.converter.HashMapConverter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Hotel.TABLE_NAME)
public class Hotel {

  protected static final String TABLE_NAME = "hotels";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonProperty(value = "hotel_id")
  private Long hotelId;

  @JsonProperty(value = "hotel_data")
  @Column(name = "hotel_data", columnDefinition = "json")
  @Convert(converter = HashMapConverter.class)
  private Map<String, Object> data = new HashMap<>();

  private Long countryId;

  @JsonIgnoreProperties({"country", "hotels"})
  @ManyToOne
  @JoinColumn(name = "city_id", referencedColumnName = "id")
  private City city;

  public void setCity(City city) {
    this.city = city;
    this.countryId = city.getCountry().getId();
  }

  @Override
  public String toString() {
    return "Hotel{" +
           "id=" + id +
           ", hotelId=" + hotelId +
           ", countryId=" + countryId +
           ", city" + city.getName() +
           '}';
  }
}
