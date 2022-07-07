package org.ujar.micro.k8s.bookingdb.importer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.micro.k8s.bookingdb.importer.service.GeoDataImporterService;
import org.ujar.micro.k8s.bookingdb.jobs.JobParameters;
import org.ujar.micro.k8s.bookingdb.jobs.JobType;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeoDataImportConsumer {
  private final GeoDataImporterService importer;

  public void consume(JobParameters parameters) {
    log.info("Received parameters: {}", parameters);
    if (parameters.getType() == JobType.IMPORT_CITIES) {
      importer.importCities();
    } else {
      importer.importCountries();
    }
  }
}
