package dev.knowhowto.bookingdb.dashboard.producer;

import dev.knowhowto.bookingdb.jobs.CitiesImportParameters;
import dev.knowhowto.bookingdb.jobs.CityHotelsImportParameters;
import dev.knowhowto.bookingdb.jobs.CountriesImportParameters;
import dev.knowhowto.bookingdb.jobs.HotelsImportParameters;
import dev.knowhowto.bookingdb.jobs.JobParameters;
import dev.knowhowto.bookingdb.jobs.amqp.AbstractProducer;
import dev.knowhowto.bookingdb.jobs.amqp.AmqpQueuesProperties;
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
