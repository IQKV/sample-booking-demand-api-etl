package org.ujar.micro.k8s.bookingdb.importer.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

  @SneakyThrows
  @Override
  public void importCities(String countryCode) {
    var countryId = countryRepository.findOneByCountry(countryCode)
        .orElseThrow(IllegalArgumentException::new)
        .getId();

    String body;
    List<City> entities;
    int offset = 0;
    do {
      body = client.getCities(countryCode, LIMIT, offset);
      var nodes = mapper.readTree(body).get("result");
      entities = mapper.convertValue(nodes,
          new TypeReference<>() {
          });

      if (entities != null) {
        var internalCityIds = entities.stream().map(City::getCityId).toList();
        var byCityId = cityRepository.findAllByCityIdIn(internalCityIds).stream().collect(
            Collectors.toMap(City::getCityId, c -> c));
        entities.forEach(city -> {
          city = byCityId.getOrDefault(city.getCityId(), city);
          city.setCountryId(countryId);
          cityRepository.save(city);
        });
        cityRepository.flush();
        offset += LIMIT;
      }
    } while (entities != null && !entities.isEmpty());
  }
}
