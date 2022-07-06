package org.ujar.micro.k8s.bookingdb.importer.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.micro.k8s.bookingdb.apiclient.client.BookingcomNetClient;
import org.ujar.micro.k8s.bookingdb.importer.service.GeoDataImporterService;
import org.ujar.micro.k8s.bookingdb.persistence.entity.Country;
import org.ujar.micro.k8s.bookingdb.persistence.repository.CountryRepository;

@Service
@Slf4j
public class GeoDataImporterServiceImpl implements GeoDataImporterService {

  private static final Integer LIMIT = 100;
  private final BookingcomNetClient client;
  private final CountryRepository countryRepository;
  private final ObjectMapper mapper;

  public GeoDataImporterServiceImpl(BookingcomNetClient client, CountryRepository countryRepository,
                                    ObjectMapper mapper) {
    this.client = client;
    this.countryRepository = countryRepository;
    this.mapper = mapper;
  }

  @SneakyThrows
  @Override
  public void importCountries() {
    String body;
    List<Country> entities;
    int offset = 0;
    do {
      body = client.getCountries(LIMIT, offset);
      log.info(body);
      var nodes = mapper.readTree(body).get("result");
      entities = mapper.convertValue(nodes,
          new TypeReference<>() {
          });

      if (entities != null) {
        var countryCodes = entities.stream().map(Country::getCountry).toList();
        var byCode = countryRepository.findAllByCountryIn(countryCodes).stream().collect(
            Collectors.toMap(Country::getCountry, c -> c));
        entities.forEach(country -> {
          country = byCode.getOrDefault(country.getCountry(), country);
          countryRepository.saveAndFlush(country);
        });
        offset += LIMIT;
      }
    } while (entities != null && !entities.isEmpty());
  }

  @Override
  public void importCities() {

  }
}
