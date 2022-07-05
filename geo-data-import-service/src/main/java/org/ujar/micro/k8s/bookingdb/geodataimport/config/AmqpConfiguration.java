package org.ujar.micro.k8s.bookingdb.geodataimport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ujar.micro.k8s.bookingdb.geodataimport.consumer.GeoDataImportConsumer;
import org.ujar.micro.k8s.bookingdb.shared.amqp.AmqpQueuesProperties;

@Configuration
@RequiredArgsConstructor
public class AmqpConfiguration {

  private final AmqpQueuesProperties queues;

  @Bean
  public SimpleMessageListenerContainer geoDataImportMessageListenerContainer(
      ConnectionFactory connectionFactory, MessageListenerAdapter geoDataImportMessageListenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queues.getGeoDataImportQueue());
    container.setMessageListener(geoDataImportMessageListenerAdapter);
    container.setDefaultRequeueRejected(false);
    return container;
  }

  @Bean
  public MessageListenerAdapter geoDataImportMessageListenerAdapter(GeoDataImportConsumer consumer) {
    return new MessageListenerAdapter(consumer, "consume");
  }
}
