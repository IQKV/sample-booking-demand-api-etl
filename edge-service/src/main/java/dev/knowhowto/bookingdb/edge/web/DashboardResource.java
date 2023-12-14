package dev.knowhowto.bookingdb.edge.web;

import jakarta.validation.constraints.NotNull;

import dev.knowhowto.bookingdb.edge.model.CityPage;
import dev.knowhowto.bookingdb.edge.model.CountryPage;
import dev.knowhowto.bookingdb.edge.model.HotelPage;
import dev.knowhowto.bookingdb.edge.service.DashboardService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
record DashboardResource(DashboardService service) {

  @QueryMapping
  Mono<CountryPage> countries(@Argument @NotNull final Integer page, @Argument final Integer size) {
    return service.countries(page, size);
  }

  @QueryMapping
  Mono<CityPage> cities(@Argument @NotNull final Integer page, @Argument final Integer size) {
    return service.cities(page, size);
  }

  @QueryMapping
  Mono<HotelPage> hotels(@Argument @NotNull final Integer page, @Argument final Integer size) {
    return service.hotels(page, size);
  }
}
