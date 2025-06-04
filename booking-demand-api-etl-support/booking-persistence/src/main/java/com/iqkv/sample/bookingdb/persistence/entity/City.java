/*
 * Copyright 2025 IQKV Foundation Team.
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

package com.iqkv.sample.bookingdemandapietl.persistence.entity;

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
