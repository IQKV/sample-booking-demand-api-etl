package com.iqkv.incubator.sample.mixbookingdb.jobs;

import com.iqkv.incubator.sample.mixbookingdb.jobs.amqp.AmqpQueuesProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AmqpQueuesProperties.class})
public class JobsConfiguration {
}
