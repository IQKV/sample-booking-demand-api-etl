/*
 * Copyright 2024 IQKV.
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

package com.iqkv.incubator.sample.mixbookingdb.edge.service;

import com.iqkv.incubator.sample.mixbookingdb.edge.config.ServicesProperties;
import com.iqkv.incubator.sample.mixbookingdb.edge.model.CityPage;
import com.iqkv.incubator.sample.mixbookingdb.edge.model.CountryPage;
import com.iqkv.incubator.sample.mixbookingdb.edge.model.HotelPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {
  private final ServicesProperties properties;

  public Mono<CountryPage> countries(Integer page, Integer size) {
    if (size == null) {
      size = 10;
    }
    final var pageSize = size;
    return WebClient.builder()
        .baseUrl(properties.dashboardService()).build().get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/countries")
            .queryParam("page", page)
            .queryParam("size", pageSize)
            .build()
        )
        .retrieve()
        .bodyToMono(CountryPage.class);
  }

  public Mono<CityPage> cities(Integer page, Integer size) {
    if (size == null) {
      size = 10;
    }
    final var pageSize = size;
    return WebClient.builder()
        .baseUrl(properties.dashboardService()).build().get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/cities")
            .queryParam("page", page)
            .queryParam("size", pageSize)
            .build()
        )
        .retrieve()
        .bodyToMono(CityPage.class);
  }

  public Mono<HotelPage> hotels(Integer page, Integer size) {
    if (size == null) {
      size = 10;
    }
    final var pageSize = size;
    return WebClient.builder()
        .baseUrl(properties.dashboardService()).build().get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/hotels")
            .queryParam("page", page)
            .queryParam("size", pageSize)
            .build()
        )
        .retrieve()
        .bodyToMono(HotelPage.class);
  }
}
