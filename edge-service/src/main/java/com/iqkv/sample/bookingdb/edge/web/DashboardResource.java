/*
 * Copyright 2025 IQKV Foundation Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.sample.bookingdemandapietl.edge.web;

import jakarta.validation.constraints.NotNull;

import com.iqkv.sample.bookingdemandapietl.edge.model.CityPage;
import com.iqkv.sample.bookingdemandapietl.edge.model.CountryPage;
import com.iqkv.sample.bookingdemandapietl.edge.model.HotelPage;
import com.iqkv.sample.bookingdemandapietl.edge.service.DashboardService;
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
