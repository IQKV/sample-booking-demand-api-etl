package com.iqkv.incubator.sample.mixbookingdb.importer.config;

import com.iqkv.incubator.sample.mixbookingdb.apiclient.annotation.EnableBookingApiClient;
import com.iqkv.incubator.sample.mixbookingdb.jobs.annotation.EnableJobSupport;
import com.iqkv.incubator.sample.mixbookingdb.persistence.annotation.EnableBookingPersistence;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJobSupport
@EnableBookingApiClient
@EnableBookingPersistence
@EnableTransactionManagement
class ApplicationConfig {
}
