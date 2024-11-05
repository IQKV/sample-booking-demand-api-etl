package com.iqkv.incubator.sample.bookingdb.importer.consumer;

import com.iqkv.incubator.sample.bookingdb.importer.service.CityImporterService;
import com.iqkv.incubator.sample.bookingdb.jobs.CitiesImportParameters;
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
