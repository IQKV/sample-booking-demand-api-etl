package org.ujar.bookingdb.importer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ujar.bookingdb.apiclient.annotation.EnableBookingApiClient;
import org.ujar.bookingdb.jobs.annotation.EnableJobSupport;
import org.ujar.bookingdb.persistence.annotation.EnableBookingPersistence;

@Configuration
@EnableJobSupport
@EnableBookingApiClient
@EnableBookingPersistence
@EnableTransactionManagement
class ApplicationConfig {
}
