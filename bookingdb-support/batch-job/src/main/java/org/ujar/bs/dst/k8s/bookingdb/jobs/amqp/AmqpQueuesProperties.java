package org.ujar.bs.dst.k8s.bookingdb.jobs.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "bookingdb.queues")
public record AmqpQueuesProperties(
    @NonNull String importExchange,
    @NonNull String importCountriesQueue,
    @NonNull String importCitiesQueue,
    @NonNull String importHotelsQueue
) {

  public String getImportExchange() {
    return importExchange();
  }

  public String getImportCountriesQueue() {
    return importCountriesQueue();
  }

  public String getImportCitiesQueue() {
    return importCitiesQueue();
  }

  public String getImportHotelsQueue() {
    return importHotelsQueue();
  }
}
