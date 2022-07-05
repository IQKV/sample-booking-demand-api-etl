package org.ujar.micro.k8s.bookingdb.geodataimport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ujar.micro.k8s.bookingdb.geodataimport.consumer.GeoDataImportConsumer;

@Configuration
@RequiredArgsConstructor
public class AmqpConfiguration {

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
  Queue geoDataImportQueue() {
    return new Queue(queues.getGeoDataImportQueue(), true);
  }

  @Bean
  TopicExchange geoDataImportExchange() {
    return new TopicExchange(queues.getGeoDataImportExchange());
  }

  @Bean
  Binding geoDataImportBinding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("geo.data.#");
  }

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
