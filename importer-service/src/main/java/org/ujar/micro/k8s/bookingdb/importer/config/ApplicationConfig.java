package org.ujar.micro.k8s.bookingdb.importer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ujar.boot.starter.logbook.LogbookJsonBodyFilter;
import org.ujar.boot.starter.logbook.LogbookResponseOnStatus;
import org.ujar.micro.k8s.bookingdb.apiclient.annotation.EnableBookingApiClient;
import org.ujar.micro.k8s.bookingdb.jobs.annotation.EnableJobSupport;
import org.ujar.micro.k8s.bookingdb.persistence.annotation.EnableBookingPersistence;

@Configuration
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
@EnableJobSupport
@EnableBookingApiClient
@EnableBookingPersistence
@EnableTransactionManagement
class ApplicationConfig {
}
