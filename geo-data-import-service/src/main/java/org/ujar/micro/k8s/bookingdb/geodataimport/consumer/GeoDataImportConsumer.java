package org.ujar.micro.k8s.bookingdb.geodataimport.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.micro.k8s.bookingdb.geodataimport.consumer.message.ImportJobParameters;
import org.ujar.micro.k8s.bookingdb.geodataimport.enums.JobType;
import org.ujar.micro.k8s.bookingdb.geodataimport.service.GeoDataImporterService;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeoDataImportConsumer {
  private final GeoDataImporterService importer;

  public void consume(ImportJobParameters message) {
    log.info("Received message: {}", message);
    if (message.getJobType() == JobType.IMPORT_CITIES) {
      importer.importCities();
    } else {
      importer.importCountries();
    }
  }
}
