package org.ujar.micro.k8s.bookingdb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = Hotel.TABLE_NAME)
public class Hotel {

  protected static final String TABLE_NAME = "hotels";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonProperty(value = "hotel_id")
  private Long hotelId;

  @JsonProperty(value = "hotel_data")
  @Type(type = "json")
  @Column(name = "hotel_data", columnDefinition = "json")
  private Map<String, String> data;

  private Long cityId;

  private Long countryId;
}
