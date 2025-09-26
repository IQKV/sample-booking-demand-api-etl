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

package com.iqkv.sample.bookingdemandapietl.importer.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqkv.sample.bookingdemandapietl.apiclient.client.BookingcomNetClient;
import com.iqkv.sample.bookingdemandapietl.importer.service.HotelImporterService;
import com.iqkv.sample.bookingdemandapietl.persistence.entity.City;
import com.iqkv.sample.bookingdemandapietl.persistence.entity.Hotel;
import com.iqkv.sample.bookingdemandapietl.persistence.repository.CityRepository;
import com.iqkv.sample.bookingdemandapietl.persistence.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HotelImporterServiceImpl implements HotelImporterService {

  private static final Integer LIMIT = 100;
  private final BookingcomNetClient client;
  private final CityRepository cityRepository;
  private final HotelRepository hotelRepository;
  private final ObjectMapper mapper;

  public HotelImporterServiceImpl(BookingcomNetClient client, CityRepository cityRepository,
                                  HotelRepository hotelRepository, ObjectMapper mapper) {
    this.client = client;
    this.cityRepository = cityRepository;
    this.hotelRepository = hotelRepository;
    this.mapper = mapper;
  }

  @Transactional
  @Override
  public void importHotels(Long cityId) {
    City city = cityRepository.findOneByCityId(cityId).orElseThrow(RuntimeException::new);
    String body;
    List<Hotel> entities;
    int offset = 0;
    do {
      body = client.getHotels(cityId, LIMIT, offset);
      JsonNode nodes;
      try {
        nodes = mapper.readTree(body).get("result");
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }

      if (nodes == null) {
        break;
      }
      entities = mapper.convertValue(nodes, new TypeReference<>() {
      });

      if (entities != null && !entities.isEmpty()) {
        entities.forEach(hotel -> {
          hotel.setCity(city);
          final var hotelId = hotel.getHotelId();
          hotelRepository.deleteByHotelId(hotelId);
          hotelRepository.saveAndFlush(hotel);
        });
      }
      offset += LIMIT;
    } while (entities != null && !entities.isEmpty());

    log.info("Import of hotels is finished.");
  }
}
