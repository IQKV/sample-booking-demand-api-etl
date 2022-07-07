package org.ujar.micro.k8s.bookingdb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = City.TABLE_NAME)
public class City {

  protected static final String TABLE_NAME = "cities";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @JsonProperty(value = "city_id")
  private Long cityId;

  private Long countryId;

  @Override
  public String toString() {
    return "City{" +
           "id=" + id +
           ", name='" + name + '\'' +
           ", cityId=" + cityId +
           ", countryId=" + countryId +
           '}';
  }
}
