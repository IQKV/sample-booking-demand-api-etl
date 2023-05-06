package org.ujar.bookingdb.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"org.ujar.bookingdb.persistence.repository"})
@EntityScan({"org.ujar.bookingdb.persistence.entity"})
@EnableJpaAuditing
@EnableTransactionManagement
public class PersistenceConfig {

}
