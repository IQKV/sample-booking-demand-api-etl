package com.iqkv.incubator.sample.mixbookingdb.jobs;

import java.time.Instant;

public interface JobParameters {

  JobType getJobType();

  Instant getPublishedAt();
}
