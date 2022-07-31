package org.ujar.micro.k8s.bookingdb.dashboard.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.boot.starter.restful.web.dto.ErrorResponse;
import org.ujar.boot.starter.restful.web.dto.PageRequestDto;
import org.ujar.micro.k8s.bookingdb.persistence.entity.Hotel;
import org.ujar.micro.k8s.bookingdb.persistence.repository.HotelRepository;

@RestController
@Tag(name = "Countries controller", description = "API for hotels management")
@Validated
@RequiredArgsConstructor
public class HotelController {
  private final HotelRepository repository;

  @GetMapping(name = "/api/hotels/{id}", produces = "application/vnd.bookingdb.api.v1+json")
  @Operation(
      description = "Retrieve hotel by id.",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "404",
                       description = "Not found",
                       content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<Hotel> findById(@PathVariable final Long id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @GetMapping(name = "/api/hotels", produces = "application/vnd.bookingdb.api.v1+json")
  @Operation(
      description = "Retrieve all hotels (with pagination).",
      responses = {
          @ApiResponse(responseCode = "200",
                       description = "Success"),
          @ApiResponse(responseCode = "500",
                       description = "Internal error",
                       content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "400",
                       description = "Bad request",
                       content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      })
  public ResponseEntity<Page<Hotel>> findAll(@ParameterObject @Valid PageRequestDto request) {
    final var pageRequest = PageRequest.of(request.getPage(), request.getSize());
    return new ResponseEntity<>(repository.findAll(pageRequest), HttpStatus.OK);
  }
}
