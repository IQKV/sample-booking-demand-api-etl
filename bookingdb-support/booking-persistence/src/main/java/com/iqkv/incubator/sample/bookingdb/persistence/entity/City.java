package com.iqkv.incubator.sample.bookingdb.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

  @ManyToOne
  @JoinColumn(name = "country_id", referencedColumnName = "id")
  private Country country;

  @JsonIgnoreProperties("city")
  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private Set<Hotel> hotels = new HashSet<>();

  @Override
  public String toString() {
    return "City{" +
           "id=" + id +
           ", name='" + name + '\'' +
           ", cityId=" + cityId +
           '}';
  }
}
