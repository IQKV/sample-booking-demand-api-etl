package org.ujar.micro.k8s.bookingdb.importer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.micro.k8s.bookingdb.apiclient.client.BookingcomNetClient;
import org.ujar.micro.k8s.bookingdb.importer.service.CountryImporterService;
import org.ujar.micro.k8s.bookingdb.persistence.entity.Country;
import org.ujar.micro.k8s.bookingdb.persistence.repository.CountryRepository;

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
      entities = mapper.convertValue(nodes, new TypeReference<>() {
      });

      if (entities != null) {
        var countryCodes = entities.stream().map(Country::getCountry).toList();
        var byCode = countryRepository.findAllByCountryIn(countryCodes)
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
