package org.ujar.bookingdb.dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.ujar.bookingdb.jobs.annotation.EnableJobSupport;
import org.ujar.bookingdb.persistence.annotation.EnableBookingPersistence;

@Configuration
@EnableJobSupport
@EnableBookingPersistence
class ApplicationConfig {
}
