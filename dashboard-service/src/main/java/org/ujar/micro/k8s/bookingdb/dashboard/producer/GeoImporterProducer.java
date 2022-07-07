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

  private static final String ROUTING_KEY_COUNTRIES = "geo.data.countries";
  private static final String ROUTING_KEY_CITIES = "geo.data.cities";

  public GeoImporterProducer(RabbitTemplate template, AmqpQueuesProperties properties) {
    super(template, properties);
  }

  public JobParameters startImportCountries(CountriesImportParameters parameters) {
    super.send(properties.getGeoDataImportExchange(), ROUTING_KEY_COUNTRIES, parameters);
    return parameters;
  }

  public JobParameters startImportCities(CitiesImportParameters parameters) {
    super.send(properties.getGeoDataImportExchange(), ROUTING_KEY_CITIES, parameters);
    return parameters;
  }
}
