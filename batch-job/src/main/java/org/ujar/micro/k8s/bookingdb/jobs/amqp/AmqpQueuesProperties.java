package org.ujar.micro.k8s.bookingdb.jobs.amqp;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@Value
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "bookingdb.queues")
public class AmqpQueuesProperties {

  @NonNull
  String geoDataImportQueue;
  @NonNull
  String geoDataImportExchange;

}
