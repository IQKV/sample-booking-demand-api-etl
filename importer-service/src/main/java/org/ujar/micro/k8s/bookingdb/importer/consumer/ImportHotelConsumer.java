package org.ujar.micro.k8s.bookingdb.importer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.micro.k8s.bookingdb.importer.service.HotelImporterService;
import org.ujar.micro.k8s.bookingdb.jobs.CityHotelsImportParameters;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportHotelConsumer {
  private final HotelImporterService importer;

  public void consume(CityHotelsImportParameters parameters) {
    log.info("Received parameters: {}", parameters);
    importer.importHotels(parameters.getCityId());
  }
}
