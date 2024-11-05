package com.iqkv.incubator.sample.bookingdb.importer.consumer;

import com.iqkv.incubator.sample.bookingdb.importer.service.CountryImporterService;
import com.iqkv.incubator.sample.bookingdb.jobs.CountriesImportParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
