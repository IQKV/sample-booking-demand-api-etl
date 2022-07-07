package org.ujar.micro.k8s.bookingdb.apiclient.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.ujar.micro.k8s.bookingdb.apiclient.*")
@EnableConfigurationProperties(NetClientProperties.class)
public class ClientConfig {
}
