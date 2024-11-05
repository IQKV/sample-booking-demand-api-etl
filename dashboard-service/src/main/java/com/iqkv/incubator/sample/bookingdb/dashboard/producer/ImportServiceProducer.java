package com.iqkv.incubator.sample.bookingdb.dashboard.producer;

import com.iqkv.incubator.sample.bookingdb.jobs.CitiesImportParameters;
import com.iqkv.incubator.sample.bookingdb.jobs.CityHotelsImportParameters;
import com.iqkv.incubator.sample.bookingdb.jobs.CountriesImportParameters;
import com.iqkv.incubator.sample.bookingdb.jobs.HotelsImportParameters;
import com.iqkv.incubator.sample.bookingdb.jobs.JobParameters;
import com.iqkv.incubator.sample.bookingdb.jobs.amqp.AbstractProducer;
import com.iqkv.incubator.sample.bookingdb.jobs.amqp.AmqpQueuesProperties;
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
