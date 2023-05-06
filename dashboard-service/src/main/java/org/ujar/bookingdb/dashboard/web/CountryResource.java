package org.ujar.bookingdb.dashboard.web;

import jakarta.validation.Valid;

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
import org.ujar.bookingdb.persistence.entity.Country;
import org.ujar.bookingdb.persistence.repository.CountryRepository;
import org.ujar.boot.restful.web.ApiError;
import org.ujar.boot.restful.web.PaginationRequest;

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
