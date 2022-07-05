package org.ujar.micro.k8s.bookingdb.geodataimport.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ujar.micro.k8s.bookingdb.geodataimport.consumer.message.ImportJobParameters;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeoDataImportConsumer {

  public void consume(ImportJobParameters message) {
    log.info("Received message: {}", message);
  }
}
