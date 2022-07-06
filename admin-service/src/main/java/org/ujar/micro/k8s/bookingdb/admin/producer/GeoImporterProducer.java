package org.ujar.micro.k8s.bookingdb.admin.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.ujar.micro.k8s.bookingdb.jobs.AbstractJobParameters;
import org.ujar.micro.k8s.bookingdb.jobs.ImportJobParameters;
import org.ujar.micro.k8s.bookingdb.jobs.JobType;
import org.ujar.micro.k8s.bookingdb.jobs.amqp.AbstractProducer;
import org.ujar.micro.k8s.bookingdb.jobs.amqp.AmqpQueuesProperties;

@Component
public class GeoImporterProducer extends AbstractProducer {

  public GeoImporterProducer(RabbitTemplate template, AmqpQueuesProperties properties) {
    super(template, properties);
  }

  public AbstractJobParameters startImportCountries(ImportJobParameters parameters) {
    parameters.setType(JobType.IMPORT_COUNTRIES);
    super.send(properties.getGeoDataImportExchange(), "geo.data.countries", parameters);
    return parameters;
  }

  public AbstractJobParameters startImportCities(ImportJobParameters parameters) {
    parameters.setType(JobType.IMPORT_CITIES);
    super.send(properties.getGeoDataImportExchange(), "geo.data.cities", parameters);
    return parameters;
  }
}
