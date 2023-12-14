package dev.knowhowto.bookingdb.importer.consumer;

import dev.knowhowto.bookingdb.importer.service.CityImporterService;
import dev.knowhowto.bookingdb.jobs.CitiesImportParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
