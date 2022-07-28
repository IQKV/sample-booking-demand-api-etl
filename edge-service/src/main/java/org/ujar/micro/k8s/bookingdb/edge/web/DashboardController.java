package org.ujar.micro.k8s.bookingdb.edge.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.ujar.micro.k8s.bookingdb.edge.model.CityPage;
import org.ujar.micro.k8s.bookingdb.edge.model.CountryPage;
import org.ujar.micro.k8s.bookingdb.edge.model.HotelPage;
import org.ujar.micro.k8s.bookingdb.edge.service.DashboardService;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService service;

  @QueryMapping
  Mono<CountryPage> countries(@Argument @NonNull Integer page, @Argument Integer size) {
    return service.countries(page, size);
  }

  @QueryMapping
  Mono<CityPage> cities(@Argument @NonNull Integer page, @Argument Integer size) {
    return service.cities(page, size);
  }

  @QueryMapping
  Mono<HotelPage> hotels(@Argument @NonNull Integer page, @Argument Integer size) {
    return service.hotels(page, size);
  }
}
