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
import org.ujar.bookingdb.persistence.entity.Hotel;
import org.ujar.bookingdb.persistence.repository.HotelRepository;
import org.ujar.boot.restful.web.ApiError;
import org.ujar.boot.restful.web.PaginationRequest;

@RestController
@Tag(name = "Countries resource", description = "API for hotels management")
@Validated
@RequiredArgsConstructor
class HotelResource {
  private final HotelRepository repository;

  @GetMapping(path = "/api/hotels/{id}", produces = "application/vnd.bookingdb.api.v1+json")
  @Operation(
      description = "Retrieve hotel by id.",
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
  ResponseEntity<Hotel> findById(@PathVariable final Long id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @GetMapping(path = "/api/hotels", produces = "application/vnd.bookingdb.api.v1+json")
  @Operation(
      description = "Retrieve all hotels (with pagination).",
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
  ResponseEntity<Page<Hotel>> findAll(@ParameterObject @Valid PaginationRequest request) {
    final var pageRequest = PageRequest.of(request.getPage(), request.getSize());
    return new ResponseEntity<>(repository.findAll(pageRequest), HttpStatus.OK);
  }
}
