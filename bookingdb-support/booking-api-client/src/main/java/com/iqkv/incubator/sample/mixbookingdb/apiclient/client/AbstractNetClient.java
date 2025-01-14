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

package com.iqkv.incubator.sample.mixbookingdb.apiclient.client;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

import com.iqkv.incubator.sample.mixbookingdb.apiclient.config.NetClientProperties;
import com.iqkv.incubator.sample.mixbookingdb.apiclient.exception.NetClientCommunicationException;
import org.springframework.web.util.UriComponentsBuilder;

public class AbstractNetClient {
  protected static final String LANGUAGE_CODE = "en";
  protected final NetClientProperties properties;
  private final HttpClient httpClient;

  public AbstractNetClient(NetClientProperties properties) {
    this.properties = properties;
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(properties.getConnectTimeout()))
        .build();
  }

  protected String doRequest(String uriPath, Map<String, String> queryParams) {
    final var uriBuilder = UriComponentsBuilder.newInstance()
        .scheme(properties.getApiSchema())
        .host(properties.apiHost())
        .path(properties.getApiPath());
    for (var queryParam : queryParams.entrySet()) {
      uriBuilder.queryParam(queryParam.getKey(), queryParam.getValue());
    }
    final var uri = uriBuilder.buildAndExpand(
        properties.getVersion(), properties.outputFormat(), uriPath
    ).toUri();
    final var auth = properties.getUsername() + ':' + properties.getPassword();
    final var encoded = Base64.getEncoder().encodeToString(auth.getBytes());
    int maxAge = 60 * 60 * 5;
    final var request = HttpRequest.newBuilder()
        .uri(uri)
        .headers(
            "Content-Type", "text/" + properties.getOutputFormat() + "; charset=utf-8",
            "Authorization", "Basic " + encoded,
            "Cache-Control", "private, max-age=" + maxAge
        )
        .timeout(Duration.ofSeconds(properties.getRequestTimeout()))
        .GET()
        .build();

    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    } catch (IOException e) {
      throw new NetClientCommunicationException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new NetClientCommunicationException(e);
    }
  }
}
