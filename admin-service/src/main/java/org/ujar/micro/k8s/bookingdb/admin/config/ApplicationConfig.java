package org.ujar.micro.k8s.bookingdb.admin.config;

import org.springframework.context.annotation.Configuration;
import org.ujar.boot.starter.logbook.LogbookJsonBodyFilter;
import org.ujar.boot.starter.logbook.LogbookResponseOnStatus;

@Configuration
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
public class ApplicationConfig {
}
