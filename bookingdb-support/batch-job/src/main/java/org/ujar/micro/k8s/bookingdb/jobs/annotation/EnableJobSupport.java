package org.ujar.micro.k8s.bookingdb.jobs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.micro.k8s.bookingdb.jobs.JobsConfiguration;
import org.ujar.micro.k8s.bookingdb.jobs.amqp.CommonAmqpConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({JobsConfiguration.class, CommonAmqpConfig.class})
@Configuration
@ComponentScan("org.ujar.micro.k8s.bookingdb.jobs")
public @interface EnableJobSupport {
}
