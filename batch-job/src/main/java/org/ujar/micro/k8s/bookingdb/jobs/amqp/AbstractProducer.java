package org.ujar.micro.k8s.bookingdb.jobs.amqp;

import java.time.Instant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.ujar.micro.k8s.bookingdb.jobs.AbstractJobParameters;

public abstract class AbstractProducer {
  protected final RabbitTemplate template;
  protected final AmqpQueuesProperties properties;

  public AbstractProducer(RabbitTemplate template, AmqpQueuesProperties properties) {
    this.template = template;
    this.properties = properties;
  }

  protected void send(String exchange, String routingKey, AbstractJobParameters parameters) {
    parameters.setPublishedAt(Instant.now());
    template.convertAndSend(exchange, routingKey, parameters);
  }
}
