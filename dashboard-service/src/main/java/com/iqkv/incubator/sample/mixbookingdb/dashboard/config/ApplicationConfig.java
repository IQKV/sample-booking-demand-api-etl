package com.iqkv.incubator.sample.mixbookingdb.dashboard.config;

import com.iqkv.incubator.sample.mixbookingdb.jobs.annotation.EnableJobSupport;
import com.iqkv.incubator.sample.mixbookingdb.persistence.annotation.EnableBookingPersistence;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJobSupport
@EnableBookingPersistence
@OpenAPIDefinition(info = @Info(title = "Bookingdb Dashboard API", version = "24.0.0"))
class ApplicationConfig {
}
