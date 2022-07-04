package org.ujar.micro.k8s.bookingdb.bookingcomclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@ConstructorBinding
@Validated
@ConfigurationProperties(prefix = "bookingdb.net-client")
public record NetClientProperties(String username, String password,
                                  String apiHost,
                                  String apiSchema,
                                  String apiPath,
                                  String version,
                                  Integer connectTimeout, Integer requestTimeout, String outputFormat) {

  public @NonNull String getUsername() {
    return username();
  }

  public @NonNull String getPassword() {
    return password();
  }

  public @NonNull String getApiHost() {
    return apiHost();
  }

  public @NonNull String getApiSchema() {
    return apiSchema();
  }


  public @NonNull String getApiPath() {
    return apiPath();
  }

  public @NonNull String getVersion() {
    return version();
  }

  public @NonNull Integer getConnectTimeout() {
    return connectTimeout();
  }

  public @NonNull Integer getRequestTimeout() {
    return requestTimeout();
  }

  public @NonNull String getOutputFormat() {
    return outputFormat();
  }
}
