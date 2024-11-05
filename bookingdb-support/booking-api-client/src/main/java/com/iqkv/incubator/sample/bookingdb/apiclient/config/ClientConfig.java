package com.iqkv.incubator.sample.bookingdb.apiclient.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.iqkv.incubator.sample.bookingdb.apiclient.*")
@EnableConfigurationProperties(NetClientProperties.class)
public class ClientConfig {
}
