package org.ujar.bs.dst.k8s.bookingdb.edge.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServicesProperties.class)
class ApplicationConfig {
}
