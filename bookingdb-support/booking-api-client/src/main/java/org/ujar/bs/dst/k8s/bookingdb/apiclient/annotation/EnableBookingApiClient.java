package org.ujar.bs.dst.k8s.bookingdb.apiclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.bs.dst.k8s.bookingdb.apiclient.config.ClientConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(ClientConfig.class)
@Configuration
public @interface EnableBookingApiClient {
}
