package dev.knowhowto.bookingdb.jobs;

import dev.knowhowto.bookingdb.jobs.amqp.AmqpQueuesProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AmqpQueuesProperties.class})
public class JobsConfiguration {
}
