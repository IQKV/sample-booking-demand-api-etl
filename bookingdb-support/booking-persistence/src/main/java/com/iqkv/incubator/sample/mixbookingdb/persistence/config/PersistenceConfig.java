package com.iqkv.incubator.sample.mixbookingdb.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"com.iqkv.incubator.sample.mixbookingdb.persistence.repository"})
@EntityScan({"com.iqkv.incubator.sample.mixbookingdb.persistence.entity"})
@EnableJpaAuditing
@EnableTransactionManagement
public class PersistenceConfig {

}
