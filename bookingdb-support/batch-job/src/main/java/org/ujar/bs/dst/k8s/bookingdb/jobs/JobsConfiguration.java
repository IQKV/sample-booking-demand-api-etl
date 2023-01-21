package org.ujar.bs.dst.k8s.bookingdb.jobs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.ujar.bs.dst.k8s.bookingdb.jobs.amqp.AmqpQueuesProperties;

@Configuration
@EnableConfigurationProperties({AmqpQueuesProperties.class})
public class JobsConfiguration {
}
