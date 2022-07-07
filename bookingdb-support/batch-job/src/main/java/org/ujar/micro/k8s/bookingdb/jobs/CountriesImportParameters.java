package org.ujar.micro.k8s.bookingdb.jobs;

import lombok.Builder;

@Builder
public class CountriesImportParameters extends AbstractJobParameters {

  @Builder.Default
  protected final JobType jobType = JobType.IMPORT_COUNTRIES;
}
