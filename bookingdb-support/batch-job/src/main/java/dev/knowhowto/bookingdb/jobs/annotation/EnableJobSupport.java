package dev.knowhowto.bookingdb.jobs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.knowhowto.bookingdb.jobs.JobsConfiguration;
import dev.knowhowto.bookingdb.jobs.amqp.CommonAmqpConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({JobsConfiguration.class, CommonAmqpConfig.class})
@Configuration
@ComponentScan("dev.knowhowto.bookingdb.jobs")
public @interface EnableJobSupport {
}
