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

package com.iqkv.sample.mixbookingdb.dashboard.producer;

import com.iqkv.sample.mixbookingdb.jobs.CitiesImportParameters;
import com.iqkv.sample.mixbookingdb.jobs.CityHotelsImportParameters;
import com.iqkv.sample.mixbookingdb.jobs.CountriesImportParameters;
import com.iqkv.sample.mixbookingdb.jobs.HotelsImportParameters;
import com.iqkv.sample.mixbookingdb.jobs.JobParameters;
import com.iqkv.sample.mixbookingdb.jobs.amqp.AbstractProducer;
import com.iqkv.sample.mixbookingdb.jobs.amqp.AmqpQueuesProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ImportServiceProducer extends AbstractProducer {
  public ImportServiceProducer(RabbitTemplate template, AmqpQueuesProperties properties) {
    super(template, properties);
  }

  public JobParameters startImportCountries(CountriesImportParameters parameters) {
    super.send(properties.getImportExchange(), "countries", parameters);
    return parameters;
  }

  public JobParameters startImportCities(CitiesImportParameters parameters) {
    super.send(properties.getImportExchange(), "cities.country." + parameters.getCountry(), parameters);
    return parameters;
  }

  public JobParameters startImportHotels(HotelsImportParameters parameters) {
    for (Long cityId : parameters.getCityIds()) {
      super.send(
          properties.getImportExchange(),
          "hotels.city." + cityId,
          CityHotelsImportParameters.builder().cityId(cityId).build()
      );
    }
    return parameters;
  }
}
