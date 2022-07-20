package org.ujar.micro.k8s.bookingdb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "country_id", referencedColumnName = "id")
  private Country country;

  @JsonIgnore
  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private Set<Hotel> hotels;

  @Override
  public String toString() {
    return "City{" +
           "id=" + id +
           ", name='" + name + '\'' +
           ", cityId=" + cityId +
           '}';
  }
}
