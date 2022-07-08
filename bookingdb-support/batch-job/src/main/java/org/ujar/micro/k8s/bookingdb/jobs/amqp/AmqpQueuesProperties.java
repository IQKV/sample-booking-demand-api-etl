package org.ujar.micro.k8s.bookingdb.jobs.amqp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "bookingdb.queues")
public class AmqpQueuesProperties {

  @NonNull
  private final String importExchange;

  @NonNull
  private final String importCountriesQueue;

  @NonNull
  private final String importCitiesQueue;

  @NonNull
  private final String importHotelsQueue;
}
