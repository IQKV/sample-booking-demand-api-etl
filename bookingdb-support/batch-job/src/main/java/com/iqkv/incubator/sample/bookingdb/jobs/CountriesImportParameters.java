package com.iqkv.incubator.sample.bookingdb.jobs;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CountriesImportParameters extends AbstractJobParameters {

  @Builder.Default
  protected final JobType jobType = JobType.IMPORT_COUNTRIES;
}
