package org.ujar.micro.k8s.bookingdb.importer.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.ujar.micro.k8s.bookingdb.apiclient.client.BookingcomNetClient;
import org.ujar.micro.k8s.bookingdb.importer.service.GeoDataImporterService;
import org.ujar.micro.k8s.bookingdb.persistence.entity.Country;
import org.ujar.micro.k8s.bookingdb.persistence.repository.CountryRepository;

@Service
public class GeoDataImporterServiceImpl implements GeoDataImporterService {

  private static final Integer LIMIT = 20;
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
      entities = mapper.convertValue(
          mapper.readTree(body).get("results"),
          new TypeReference<>() {
          });
      countryRepository.saveAllAndFlush(entities);
      offset += LIMIT;
    } while (!entities.isEmpty());
  }

  @Override
  public void importCities() {

  }
}
