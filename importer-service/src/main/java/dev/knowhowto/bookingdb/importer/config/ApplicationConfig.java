package dev.knowhowto.bookingdb.importer.config;

import dev.knowhowto.bookingdb.apiclient.annotation.EnableBookingApiClient;
import dev.knowhowto.bookingdb.jobs.annotation.EnableJobSupport;
import dev.knowhowto.bookingdb.persistence.annotation.EnableBookingPersistence;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJobSupport
@EnableBookingApiClient
@EnableBookingPersistence
@EnableTransactionManagement
class ApplicationConfig {
}
