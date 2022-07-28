package org.ujar.micro.k8s.bookingdb.edge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConstructorBinding
@Validated
@ConfigurationProperties(prefix = "ujar.services")
public record ServicesProperties(String dashboardService) {
}
