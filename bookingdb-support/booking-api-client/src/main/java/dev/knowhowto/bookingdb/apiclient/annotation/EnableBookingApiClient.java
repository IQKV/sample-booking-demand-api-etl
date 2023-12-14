package dev.knowhowto.bookingdb.apiclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.knowhowto.bookingdb.apiclient.config.ClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(ClientConfig.class)
@Configuration
public @interface EnableBookingApiClient {
}
