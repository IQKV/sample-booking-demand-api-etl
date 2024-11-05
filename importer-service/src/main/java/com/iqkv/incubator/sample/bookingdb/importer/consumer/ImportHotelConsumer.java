package com.iqkv.incubator.sample.bookingdb.importer.consumer;

import com.iqkv.incubator.sample.bookingdb.importer.service.HotelImporterService;
import com.iqkv.incubator.sample.bookingdb.jobs.CityHotelsImportParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
