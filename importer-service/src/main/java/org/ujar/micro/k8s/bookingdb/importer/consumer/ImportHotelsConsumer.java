package org.ujar.micro.k8s.bookingdb.importer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.micro.k8s.bookingdb.importer.service.HotelImporterService;
import org.ujar.micro.k8s.bookingdb.jobs.HotelsImportParameters;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportHotelsConsumer {
  private final HotelImporterService importer;

  public void consume(HotelsImportParameters parameters) {
    log.info("Received parameters: {}", parameters);
    importer.importHotels(parameters.getCityId());
  }
}
