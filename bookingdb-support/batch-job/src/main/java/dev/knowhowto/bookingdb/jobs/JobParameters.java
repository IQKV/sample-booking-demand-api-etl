package dev.knowhowto.bookingdb.jobs;

import java.time.Instant;

public interface JobParameters {

  JobType getJobType();

  Instant getPublishedAt();
}
