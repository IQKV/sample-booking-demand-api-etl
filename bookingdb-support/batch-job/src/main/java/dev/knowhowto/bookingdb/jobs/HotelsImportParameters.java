package dev.knowhowto.bookingdb.jobs;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class HotelsImportParameters extends AbstractJobParameters {

  private List<Long> cityIds;

  @Builder.Default
  protected final JobType type = JobType.IMPORT_HOTELS;

}
