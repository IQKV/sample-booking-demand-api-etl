package com.iqkv.incubator.sample.bookingdb.importer.service.impl;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqkv.incubator.sample.bookingdb.apiclient.client.BookingcomNetClient;
import com.iqkv.incubator.sample.bookingdb.importer.service.CityImporterService;
import com.iqkv.incubator.sample.bookingdb.persistence.entity.City;
import com.iqkv.incubator.sample.bookingdb.persistence.repository.CityRepository;
import com.iqkv.incubator.sample.bookingdb.persistence.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityImporterServiceImpl implements CityImporterService {

  private static final Integer LIMIT = 100;
  private final BookingcomNetClient client;
  private final CityRepository cityRepository;

  private final CountryRepository countryRepository;
  private final ObjectMapper mapper;

  @Transactional
  @Override
  public void importCities(final String countryCode) {
    final var country = countryRepository.findOneByCountry(countryCode)
        .orElseThrow(IllegalArgumentException::new);

    String body;
    final List<City> entities = new ArrayList<>();
    int offset = 0;
    do {
      body = client.getCities(countryCode, LIMIT, offset);
      JsonNode nodes;
      try {
        nodes = mapper.readTree(body).get("result");

      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }

      if (nodes == null) {
        break;
      }

      for (var node : nodes) {
        entities.add(new City(null, node.get("name").textValue(), node.get("city_id").longValue(), country, Set.of()));
      }

      final var internalCityIds = entities.stream().map(City::getCityId).toList();
      final var byCityId = cityRepository.findAllByCityIdIn(internalCityIds)
          .stream()
          .collect(Collectors.toMap(City::getCityId, Function.identity()));

      entities.forEach(city -> {
        city = byCityId.getOrDefault(city.getCityId(), city);
        city.setCountry(country);
        cityRepository.save(city);
      });

      cityRepository.flush();

      offset += LIMIT;
    } while (entities != null && !entities.isEmpty());

    log.info("Import of cities batch is finished.");
  }
}
