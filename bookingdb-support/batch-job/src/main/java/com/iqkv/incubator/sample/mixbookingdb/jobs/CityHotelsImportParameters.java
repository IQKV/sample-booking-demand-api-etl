package com.iqkv.incubator.sample.mixbookingdb.jobs;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CityHotelsImportParameters extends AbstractJobParameters {

  private Long cityId;

  @Builder.Default
  protected final JobType type = JobType.IMPORT_HOTELS;

}
