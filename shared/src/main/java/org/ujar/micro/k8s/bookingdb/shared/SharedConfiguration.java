package org.ujar.micro.k8s.bookingdb.shared;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.ujar.micro.k8s.bookingdb.shared.amqp.AmqpQueuesProperties;

@Configuration
@EnableConfigurationProperties({AmqpQueuesProperties.class})
public class SharedConfiguration {
}
