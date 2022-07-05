package org.ujar.micro.k8s.bookingdb.geodataimport.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.ujar.micro.k8s.bookingdb.bookingcomclient.client.BookingcomNetClient;
import org.ujar.micro.k8s.bookingdb.geodataimport.entity.CountryEntity;
import org.ujar.micro.k8s.bookingdb.geodataimport.repository.CountryRepository;

@Service
public class GeoDataImporterServiceImpl implements GeoDataImporterService {

  private final static Integer LIMIT = 20;
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
    List<CountryEntity> entities;
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
