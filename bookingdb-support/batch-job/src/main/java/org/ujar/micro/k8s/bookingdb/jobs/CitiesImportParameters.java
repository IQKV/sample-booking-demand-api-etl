package org.ujar.micro.k8s.bookingdb.jobs;

import lombok.Builder;

@Builder
public class CitiesImportParameters extends AbstractJobParameters {

  private String country;

  protected JobType type = JobType.IMPORT_CITIES;

}
