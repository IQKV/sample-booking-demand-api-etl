package org.ujar.bookingdb.jobs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.bookingdb.jobs.JobsConfiguration;
import org.ujar.bookingdb.jobs.amqp.CommonAmqpConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({JobsConfiguration.class, CommonAmqpConfig.class})
@Configuration
@ComponentScan("org.ujar.bookingdb.jobs")
public @interface EnableJobSupport {
}
