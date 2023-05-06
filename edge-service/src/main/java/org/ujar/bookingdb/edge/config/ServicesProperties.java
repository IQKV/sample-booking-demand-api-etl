package org.ujar.bookingdb.edge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ujar.services")
public record ServicesProperties(String dashboardService) {
}
