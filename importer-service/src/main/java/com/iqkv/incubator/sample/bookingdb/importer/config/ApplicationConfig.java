package com.iqkv.incubator.sample.bookingdb.importer.config;

import com.iqkv.incubator.sample.bookingdb.apiclient.annotation.EnableBookingApiClient;
import com.iqkv.incubator.sample.bookingdb.jobs.annotation.EnableJobSupport;
import com.iqkv.incubator.sample.bookingdb.persistence.annotation.EnableBookingPersistence;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJobSupport
@EnableBookingApiClient
@EnableBookingPersistence
@EnableTransactionManagement
class ApplicationConfig {
}
