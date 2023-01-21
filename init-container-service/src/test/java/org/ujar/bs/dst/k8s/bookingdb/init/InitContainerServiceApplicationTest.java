package org.ujar.bs.dst.k8s.bookingdb.init;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InitContainerServiceApplicationTest {

  @Test
  void contextLoad() {
    Assertions.assertDoesNotThrow(this::doNotThrowException);
  }

  private void doNotThrowException() {
    // This method will never throw exception
  }
}
