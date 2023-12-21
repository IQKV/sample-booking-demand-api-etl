package dev.knowhowto.bookingdb.dashboard.config;

import dev.knowhowto.bookingdb.jobs.annotation.EnableJobSupport;
import dev.knowhowto.bookingdb.persistence.annotation.EnableBookingPersistence;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJobSupport
@EnableBookingPersistence
@OpenAPIDefinition(info = @Info(title = "Bookingdb Dashboard API", version = "24.0.0"))
class ApplicationConfig {
}
