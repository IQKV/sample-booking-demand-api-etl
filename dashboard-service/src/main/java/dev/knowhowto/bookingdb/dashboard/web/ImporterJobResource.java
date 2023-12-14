package dev.knowhowto.bookingdb.dashboard.web;

import java.util.List;

import dev.knowhowto.bookingdb.dashboard.producer.ImportServiceProducer;
import dev.knowhowto.bookingdb.jobs.CitiesImportParameters;
import dev.knowhowto.bookingdb.jobs.CountriesImportParameters;
import dev.knowhowto.bookingdb.jobs.HotelsImportParameters;
import dev.knowhowto.bookingdb.jobs.JobParameters;
import dev.knowhowto.bookingdb.persistence.entity.Country;
import dev.knowhowto.bookingdb.persistence.repository.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.iqkv.boot.restful.web.ApiError;
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

  @PostMapping(path = "/api/import/countries", produces = "application/vnd.bookingdb.api.v1+json")
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

  @PostMapping(path = "/api/import/cities", produces = "application/vnd.bookingdb.api.v1+json")
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

  @PostMapping(path = "/api/import/cities/{country}", produces = "application/vnd.bookingdb.api.v1+json")
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

  @PostMapping(path = "/api/import/hotels", produces = "application/vnd.bookingdb.api.v1+json")
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
