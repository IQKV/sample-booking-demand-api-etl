package com.iqkv.incubator.sample.mixbookingdb.edge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iqkv.service-discovery.services")
public record ServicesProperties(String dashboardService) {
}
