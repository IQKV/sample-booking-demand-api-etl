/*
 * Copyright 2024 IQKV.
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

package com.iqkv.incubator.sample.mixbookingdb.importer.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqkv.incubator.sample.mixbookingdb.apiclient.client.BookingcomNetClient;
import com.iqkv.incubator.sample.mixbookingdb.importer.service.CountryImporterService;
import com.iqkv.incubator.sample.mixbookingdb.persistence.entity.Country;
import com.iqkv.incubator.sample.mixbookingdb.persistence.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CountryImporterServiceImpl implements CountryImporterService {

  private static final Integer LIMIT = 100;
  private final BookingcomNetClient client;
  private final CountryRepository countryRepository;
  private final ObjectMapper mapper;

  public CountryImporterServiceImpl(BookingcomNetClient client,
                                    CountryRepository countryRepository,
                                    ObjectMapper mapper) {
    this.client = client;
    this.countryRepository = countryRepository;
    this.mapper = mapper;
  }

  @Transactional
  @Override
  public void importCountries() {
    String body;
    List<Country> entities;
    int offset = 0;
    do {
      body = client.getCountries(LIMIT, offset);
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

      if (entities != null) {
        final var countryCodes = entities.stream().map(Country::getCountry).toList();
        final var byCode = countryRepository.findAllByCountryIn(countryCodes)
            .stream()
            .collect(Collectors.toMap(Country::getCountry, Function.identity()));

        entities.forEach(country -> {
          country = byCode.getOrDefault(country.getCountry(), country);
          countryRepository.save(country);
        });

        countryRepository.flush();

        offset += LIMIT;
      }
    } while (entities != null && !entities.isEmpty());

    log.info("Import of countries is finished.");
  }
}
