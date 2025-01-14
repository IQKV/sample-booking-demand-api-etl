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

package com.iqkv.incubator.sample.mixbookingdb.importer.consumer;

import com.iqkv.incubator.sample.mixbookingdb.importer.service.HotelImporterService;
import com.iqkv.incubator.sample.mixbookingdb.jobs.CityHotelsImportParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportHotelConsumer {
  private final HotelImporterService importer;

  public void consume(CityHotelsImportParameters parameters) {
    log.info("Received parameters: {}", parameters);
    importer.importHotels(parameters.getCityId());
  }
}
