package org.ujar.bs.dst.k8s.bookingdb.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InitContainerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(InitContainerServiceApplication.class, args);
  }
}
