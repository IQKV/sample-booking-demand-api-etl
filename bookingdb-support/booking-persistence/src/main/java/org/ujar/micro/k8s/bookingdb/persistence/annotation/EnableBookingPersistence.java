package org.ujar.micro.k8s.bookingdb.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.micro.k8s.bookingdb.persistence.config.CacheConfig;
import org.ujar.micro.k8s.bookingdb.persistence.config.PersistenceConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Configuration
@Import({PersistenceConfig.class, CacheConfig.class})
public @interface EnableBookingPersistence {
}
