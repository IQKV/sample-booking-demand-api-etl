package org.ujar.bookingdb.dashboard.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import org.ujar.bookingdb.jobs.annotation.EnableJobSupport;
import org.ujar.bookingdb.persistence.annotation.EnableBookingPersistence;

@Configuration
@EnableJobSupport
@EnableBookingPersistence
@OpenAPIDefinition(info = @Info(title = "Bookingdb Dashboard API", version = "23.0.0"))
class ApplicationConfig {
}
