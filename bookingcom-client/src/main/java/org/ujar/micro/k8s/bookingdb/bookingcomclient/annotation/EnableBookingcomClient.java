package org.ujar.micro.k8s.bookingdb.bookingcomclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.micro.k8s.bookingdb.bookingcomclient.config.ClientConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(ClientConfig.class)
@Configuration
public @interface EnableBookingcomClient {
}
