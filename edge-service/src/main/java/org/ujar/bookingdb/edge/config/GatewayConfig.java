package org.ujar.bookingdb.edge.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
class GatewayConfig {

  @Bean
  RouteLocator gateway(RouteLocatorBuilder rlb, ServicesProperties properties) {
    return
        rlb
            .routes()
            .route(rs ->
                rs.path("/")
                    .filters(fs -> fs
                        .setPath("/dashboard/")
                        .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                        .retry(3)
                    )
                    .uri(properties.dashboardService())
            )
            .build();
  }
}
