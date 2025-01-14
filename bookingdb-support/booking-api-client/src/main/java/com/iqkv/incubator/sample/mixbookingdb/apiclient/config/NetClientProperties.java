/*
 * Copyright 2024 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixbookingdb.apiclient.config;

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
