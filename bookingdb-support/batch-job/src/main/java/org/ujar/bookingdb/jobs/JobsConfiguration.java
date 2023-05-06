package org.ujar.bookingdb.jobs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.ujar.bookingdb.jobs.amqp.AmqpQueuesProperties;

@Configuration
@EnableConfigurationProperties({AmqpQueuesProperties.class})
public class JobsConfiguration {
}
