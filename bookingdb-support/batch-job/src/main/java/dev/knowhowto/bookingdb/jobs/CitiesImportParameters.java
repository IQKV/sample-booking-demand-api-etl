package dev.knowhowto.bookingdb.jobs;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CitiesImportParameters extends AbstractJobParameters {

  private String country;

  @Builder.Default
  protected final JobType type = JobType.IMPORT_CITIES;

}
