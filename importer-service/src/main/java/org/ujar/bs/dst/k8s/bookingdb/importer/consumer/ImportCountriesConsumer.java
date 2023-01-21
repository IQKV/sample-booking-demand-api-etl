package org.ujar.bs.dst.k8s.bookingdb.importer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.bs.dst.k8s.bookingdb.importer.service.CountryImporterService;
import org.ujar.bs.dst.k8s.bookingdb.jobs.CountriesImportParameters;

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
