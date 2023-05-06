package org.ujar.bookingdb.apiclient.config;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookingdb.net-client")
public record NetClientProperties(String username, String password,
                                  String apiHost,
                                  String apiSchema,
                                  String apiPath,
                                  String version,
                                  Integer connectTimeout, Integer requestTimeout, String outputFormat) {

  public @NotNull String getUsername() {
    return username();
  }

  public @NotNull String getPassword() {
    return password();
  }

  public @NotNull String getApiHost() {
    return apiHost();
  }

  public @NotNull String getApiSchema() {
    return apiSchema();
  }


  public @NotNull String getApiPath() {
    return apiPath();
  }

  public @NotNull String getVersion() {
    return version();
  }

  public @NotNull Integer getConnectTimeout() {
    return connectTimeout();
  }

  public @NotNull Integer getRequestTimeout() {
    return requestTimeout();
  }

  public @NotNull String getOutputFormat() {
    return outputFormat();
  }
}
