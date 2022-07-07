package org.ujar.micro.k8s.bookingdb.dashboard.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.ujar.micro.k8s.bookingdb.jobs.CitiesImportParameters;
import org.ujar.micro.k8s.bookingdb.jobs.CountriesImportParameters;
import org.ujar.micro.k8s.bookingdb.jobs.JobParameters;
import org.ujar.micro.k8s.bookingdb.jobs.amqp.AbstractProducer;
import org.ujar.micro.k8s.bookingdb.jobs.amqp.AmqpQueuesProperties;

@Component
public class GeoImporterProducer extends AbstractProducer {
  public GeoImporterProducer(RabbitTemplate template, AmqpQueuesProperties properties) {
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
}
