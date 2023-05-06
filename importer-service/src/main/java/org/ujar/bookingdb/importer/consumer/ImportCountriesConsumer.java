package org.ujar.bookingdb.importer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.bookingdb.importer.service.CountryImporterService;
import org.ujar.bookingdb.jobs.CountriesImportParameters;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportCountriesConsumer {
  private final CountryImporterService countryImporter;

  public void consume(CountriesImportParameters parameters) {
    log.info("Received parameters: {}", parameters);
    countryImporter.importCountries();
  }
}
