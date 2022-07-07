package org.ujar.micro.k8s.bookingdb.importer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ujar.micro.k8s.bookingdb.importer.consumer.GeoDataImportConsumer;
import org.ujar.micro.k8s.bookingdb.jobs.amqp.AmqpQueuesProperties;

@Configuration
@RequiredArgsConstructor
public class AmqpConfiguration {

  private final AmqpQueuesProperties queues;

  @Bean
  public SimpleMessageListenerContainer geoDataImportMessageListenerContainer(
      final ConnectionFactory connectionFactory, final MessageListenerAdapter geoDataImportMessageListenerAdapter) {
    final var container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queues.getGeoDataImportQueue());
    container.setMessageListener(geoDataImportMessageListenerAdapter);
    container.setDefaultRequeueRejected(false);
    return container;
  }

  @Bean
  public MessageListenerAdapter geoDataImportMessageListenerAdapter(final GeoDataImportConsumer consumer) {
    return new MessageListenerAdapter(consumer, "consume");
  }
}
