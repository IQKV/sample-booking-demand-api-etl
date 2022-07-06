package org.ujar.micro.k8s.bookingdb.jobs;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
public abstract class AbstractJobParameters implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;


  private @NonNull JobType type;

  private @NonNull Instant publishedAt;
}
