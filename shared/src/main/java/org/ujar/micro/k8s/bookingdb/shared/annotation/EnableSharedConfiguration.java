package org.ujar.micro.k8s.bookingdb.shared.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.micro.k8s.bookingdb.shared.SharedConfiguration;
import org.ujar.micro.k8s.bookingdb.shared.amqp.CommonAmqpConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({SharedConfiguration.class, CommonAmqpConfiguration.class})
@Configuration
@ComponentScan("org.ujar.micro.k8s.bookingdb.shared")
public @interface EnableSharedConfiguration {
}
