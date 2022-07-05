package org.ujar.micro.k8s.bookingdb.geodataimport.consumer.message;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ujar.micro.k8s.bookingdb.geodataimport.enums.JobType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportJobParameters implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private JobType jobType;
  private Instant publishedAt;
}


