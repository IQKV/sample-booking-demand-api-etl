package com.iqkv.incubator.sample.mixbookingdb.importer.consumer;

import com.iqkv.incubator.sample.mixbookingdb.importer.service.CountryImporterService;
import com.iqkv.incubator.sample.mixbookingdb.jobs.CountriesImportParameters;
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
