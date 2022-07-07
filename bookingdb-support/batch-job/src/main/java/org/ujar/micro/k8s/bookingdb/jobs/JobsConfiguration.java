package org.ujar.micro.k8s.bookingdb.jobs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.ujar.micro.k8s.bookingdb.jobs.amqp.AmqpQueuesProperties;

@Configuration
@EnableConfigurationProperties({AmqpQueuesProperties.class})
public class JobsConfiguration {
}
