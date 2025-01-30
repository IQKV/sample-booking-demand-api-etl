/*
 * Copyright 2025 IQKV Team.
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

package com.iqkv.incubator.sample.mixbookingdb.dashboard.web;

import jakarta.validation.Valid;

import com.iqkv.boot.mvc.rest.ApiError;
import com.iqkv.boot.mvc.rest.PaginationRequest;
import com.iqkv.incubator.sample.mixbookingdb.persistence.entity.Country;
import com.iqkv.incubator.sample.mixbookingdb.persistence.repository.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Countries resource", description = "API for countries management")
@Validated
@RequiredArgsConstructor
class CountryResource {
  private final CountryRepository repository;

  @GetMapping(path = "/api/countries/{id}", produces = "application/vnd.bookingdb.api.v1+json")
  @Operation(
      description = "Retrieve country by id.",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
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
  ResponseEntity<Country> findById(@PathVariable final Long id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @GetMapping(path = "/api/countries", produces = "application/vnd.bookingdb.api.v1+json")
  @Operation(
      description = "Retrieve all countries (with pagination).",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ApiError.class))),
      })
  ResponseEntity<Page<Country>> findAll(@ParameterObject @Valid final PaginationRequest request) {
    final var pageRequest = PageRequest.of(request.getPage(), request.getSize());
    return new ResponseEntity<>(repository.findAll(pageRequest), HttpStatus.OK);
  }
}
