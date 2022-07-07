package org.ujar.micro.k8s.bookingdb.jobs;

import java.time.Instant;

public interface JobParameters  {

  JobType getType();

  Instant getPublishedAt();
}
