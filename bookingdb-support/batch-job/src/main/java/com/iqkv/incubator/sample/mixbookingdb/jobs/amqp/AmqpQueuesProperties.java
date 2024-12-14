package com.iqkv.incubator.sample.mixbookingdb.jobs.amqp;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookingdb.queues")
public record AmqpQueuesProperties(
    @NotNull String importExchange,
    @NotNull String importCountriesQueue,
    @NotNull String importCitiesQueue,
    @NotNull String importHotelsQueue
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
