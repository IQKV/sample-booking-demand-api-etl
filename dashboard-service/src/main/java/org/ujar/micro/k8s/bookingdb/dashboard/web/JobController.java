package org.ujar.micro.k8s.bookingdb.dashboard.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.boot.starter.restful.web.dto.ErrorResponse;
import org.ujar.micro.k8s.bookingdb.dashboard.producer.GeoImporterProducer;
import org.ujar.micro.k8s.bookingdb.jobs.AbstractJobParameters;
import org.ujar.micro.k8s.bookingdb.jobs.ImportJobParameters;

@RestController
@Tag(name = "Jobs controller", description = "API for job management")
@RequestMapping("/api/v1/job")
@Validated
@RequiredArgsConstructor
public class JobController {

  private final GeoImporterProducer producer;

  @PutMapping("/importCountries")
  @Operation(
      description = "Start geo data import job.",
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
  public ResponseEntity<AbstractJobParameters> findById() {
    return new ResponseEntity<>(producer.startImportCountries(ImportJobParameters.builder().build()), HttpStatus.OK);
  }
}
