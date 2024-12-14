package com.iqkv.incubator.sample.mixbookingdb.importer.consumer;

import com.iqkv.incubator.sample.mixbookingdb.importer.service.HotelImporterService;
import com.iqkv.incubator.sample.mixbookingdb.jobs.CityHotelsImportParameters;
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
