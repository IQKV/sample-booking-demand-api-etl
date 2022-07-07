package org.ujar.micro.k8s.bookingdb.jobs;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CitiesImportParameters extends AbstractJobParameters {

  private String country;

  @Builder.Default
  protected final JobType type = JobType.IMPORT_CITIES;

}
