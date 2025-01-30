/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixbookingdb.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iqkv.incubator.sample.mixbookingdb.persistence.converter.HashMapConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
