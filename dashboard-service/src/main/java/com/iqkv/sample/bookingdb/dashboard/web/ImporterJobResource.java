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

package com.iqkv.sample.bookingdemandapietl.dashboard.web;

import java.util.List;

import com.iqkv.boot.mvc.rest.ApiError;
import com.iqkv.sample.bookingdemandapietl.dashboard.producer.ImportServiceProducer;
import com.iqkv.sample.bookingdemandapietl.jobs.CitiesImportParameters;
import com.iqkv.sample.bookingdemandapietl.jobs.CountriesImportParameters;
import com.iqkv.sample.bookingdemandapietl.jobs.HotelsImportParameters;
import com.iqkv.sample.bookingdemandapietl.jobs.JobParameters;
import com.iqkv.sample.bookingdemandapietl.persistence.entity.Country;
import com.iqkv.sample.bookingdemandapietl.persistence.repository.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Importer job resource", description = "API for job management")
@Validated
@RequiredArgsConstructor
class ImporterJobResource {

  private final ImportServiceProducer producer;
  private final CountryRepository countryRepository;

  @PostMapping(path = "/api/import/countries", produces = "application/vnd.bookingdemandapietl.api.v1+json")
  @Operation(
      description = "Start countries list import job.",
      responses = {
          @ApiResponse(responseCode = "202",
                       description = "Accepted"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "404",
                       description = "Not found",
                       content = @Content(schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<JobParameters> countries() {
    return new ResponseEntity<>(
        producer.startImportCountries(CountriesImportParameters.builder().build()),
        HttpStatus.ACCEPTED
    );
  }

  @PostMapping(path = "/api/import/cities", produces = "application/vnd.bookingdemandapietl.api.v1+json")
  @Operation(
      description = "Start all cities list import job.",
      responses = {
          @ApiResponse(responseCode = "202",
                       description = "Accepted"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "404",
                       description = "Not found",
                       content = @Content(schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<List<JobParameters>> cities() {
    final var started = countryRepository.findAll().stream()
        .map(Country::getCountry)
        .map(this::startCitiesImport)
        .toList();
    return new ResponseEntity<>(started, HttpStatus.ACCEPTED);
  }

  @PostMapping(path = "/api/import/cities/{country}", produces = "application/vnd.bookingdemandapietl.api.v1+json")
  @Operation(
      description = "Start country cities list import job.",
      responses = {
          @ApiResponse(responseCode = "202",
                       description = "Accepted"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "404",
                       description = "Not found",
                       content = @Content(schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<JobParameters> cities(@PathVariable String country) {
    return new ResponseEntity<>(
        startCitiesImport(country),
        HttpStatus.ACCEPTED
    );
  }

  @PostMapping(path = "/api/import/hotels", produces = "application/vnd.bookingdemandapietl.api.v1+json")
  @Operation(
      description = "Start importing all hotels in the particular cities.",
      responses = {
          @ApiResponse(responseCode = "202",
                       description = "Accepted"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "404",
                       description = "Not found",
                       content = @Content(schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<JobParameters> hotels(@RequestBody final List<Long> cityIds) {
    return new ResponseEntity<>(
        producer.startImportHotels(HotelsImportParameters.builder().cityIds(cityIds).build()),
        HttpStatus.ACCEPTED
    );
  }

  private JobParameters startCitiesImport(final String country) {
    return producer.startImportCities(
        CitiesImportParameters.builder()
            .country(country)
            .build());
  }
}
