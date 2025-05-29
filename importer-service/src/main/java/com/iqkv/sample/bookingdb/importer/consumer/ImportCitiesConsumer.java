/*
 * Copyright 2025 IQKV Foundation Team.
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

package com.iqkv.sample.bookingdb.importer.consumer;

import com.iqkv.sample.bookingdb.importer.service.CityImporterService;
import com.iqkv.sample.bookingdb.jobs.CitiesImportParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportCitiesConsumer {
  private final CityImporterService importer;

  public void consume(CitiesImportParameters parameters) {
    log.info("Received parameters: {}", parameters);
    importer.importCities(parameters.getCountry());
  }
}
