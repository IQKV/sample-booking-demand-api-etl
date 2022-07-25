package org.ujar.micro.k8s.bookingdb.importer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.micro.k8s.bookingdb.apiclient.client.BookingcomNetClient;
import org.ujar.micro.k8s.bookingdb.importer.service.CityImporterService;
import org.ujar.micro.k8s.bookingdb.persistence.entity.City;
import org.ujar.micro.k8s.bookingdb.persistence.repository.CityRepository;
import org.ujar.micro.k8s.bookingdb.persistence.repository.CountryRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityImporterServiceImpl implements CityImporterService {

  private static final Integer LIMIT = 100;
  private final BookingcomNetClient client;
  private final CityRepository cityRepository;

  private final CountryRepository countryRepository;
  private final ObjectMapper mapper;

  @Override
  public void importCities(String countryCode) {
    var country = countryRepository.findOneByCountry(countryCode)
        .orElseThrow(IllegalArgumentException::new);

    String body;
    List<City> entities = new ArrayList<>();
    int offset = 0;
    do {
      body = client.getCities(countryCode, LIMIT, offset);
      JsonNode nodes;
      try {
        nodes = mapper.readTree(body).get("result");

      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }

      for (var node : nodes) {
        entities.add(new City(null, node.get("name").textValue(), node.get("city_id").longValue(), country, Set.of()));
      }

      var internalCityIds = entities.stream().map(City::getCityId).toList();
      var byCityId = cityRepository.findAllByCityIdIn(internalCityIds)
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
