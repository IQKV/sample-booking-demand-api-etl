/*
 * Copyright 2025 IQKV Team.
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

package com.iqkv.incubator.sample.mixbookingdb.jobs.amqp;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookingdb.queues")
public record AmqpQueuesProperties(
    @NotNull String importExchange,
    @NotNull String importCountriesQueue,
    @NotNull String importCitiesQueue,
    @NotNull String importHotelsQueue
) {

  public String getImportExchange() {
    return importExchange();
  }

  public String getImportCountriesQueue() {
    return importCountriesQueue();
  }

  public String getImportCitiesQueue() {
    return importCitiesQueue();
  }

  public String getImportHotelsQueue() {
    return importHotelsQueue();
  }
}
