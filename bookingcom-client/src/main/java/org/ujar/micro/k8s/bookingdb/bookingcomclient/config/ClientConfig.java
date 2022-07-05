package org.ujar.micro.k8s.bookingdb.bookingcomclient.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.ujar.micro.k8s.bookingdb.bookingcomclient.*")
@EnableConfigurationProperties(NetClientProperties.class)
public class ClientConfig {
}
