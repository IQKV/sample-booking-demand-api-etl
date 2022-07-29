package org.ujar.micro.k8s.bookingdb.jobs.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CommonAmqpConfig {

  private final AmqpQueuesProperties queues;

  @Bean
  AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    return new RabbitTemplate(connectionFactory);
  }

  @Bean
  Queue importCountriesQueue() {
    return new Queue(queues.getImportCountriesQueue(),
        true, false, true);
  }

  @Bean
  Queue importCitiesQueue() {
    return new Queue(queues.getImportCitiesQueue(),
        true, false, true);
  }

  @Bean
  Queue importHotelsQueue() {
    return new Queue(queues.getImportHotelsQueue(),
        true, false, true);
  }

  @Bean
  TopicExchange importTopicExchange() {
    return new TopicExchange(queues.getImportExchange());
  }

  @Bean
  public Declarables topicBindings(Queue importCountriesQueue,
                                   Queue importCitiesQueue,
                                   Queue importHotelsQueue,
                                   TopicExchange importTopicExchange) {
    return new Declarables(
        importCountriesQueue,
        importCitiesQueue,
        importHotelsQueue,
        importTopicExchange,
        BindingBuilder
            .bind(importCountriesQueue)
            .to(importTopicExchange).with("countries"),
        BindingBuilder
            .bind(importCitiesQueue)
            .to(importTopicExchange).with("cities.country.*"),
        BindingBuilder
            .bind(importHotelsQueue)
            .to(importTopicExchange).with("hotels.city.*")
    );
  }
}
