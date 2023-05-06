package org.ujar.bookingdb.importer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.bookingdb.importer.service.CityImporterService;
import org.ujar.bookingdb.jobs.CitiesImportParameters;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportCitiesConsumer {
  private final CityImporterService importer;

  public void consume(CitiesImportParameters parameters) {
    log.info("Received parameters: {}", parameters);
    importer.importCities(parameters.getCountry());
  }
}
