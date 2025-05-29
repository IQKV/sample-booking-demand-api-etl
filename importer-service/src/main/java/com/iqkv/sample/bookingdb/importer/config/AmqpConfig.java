/*
 * Copyright 2025 IQKV Foundation Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.sample.bookingdb.importer.config;

import com.iqkv.sample.bookingdb.importer.consumer.ImportCitiesConsumer;
import com.iqkv.sample.bookingdb.importer.consumer.ImportCountriesConsumer;
import com.iqkv.sample.bookingdb.importer.consumer.ImportHotelConsumer;
import com.iqkv.sample.bookingdb.jobs.amqp.AmqpQueuesProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmqpConfig {

  private final AmqpQueuesProperties queues;

  @Bean
  public SimpleMessageListenerContainer importCountriesListenerContainer(
      final ConnectionFactory connectionFactory, final MessageListenerAdapter importCountriesListenerAdapter) {
    final var container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queues.getImportCountriesQueue());
    container.setMessageListener(importCountriesListenerAdapter);
    container.setAcknowledgeMode(AcknowledgeMode.AUTO);
    container.setDefaultRequeueRejected(false);
    return container;
  }

  @Bean
  public MessageListenerAdapter importCountriesListenerAdapter(final ImportCountriesConsumer consumer) {
    return new MessageListenerAdapter(consumer, "consume");
  }

  @Bean
  public SimpleMessageListenerContainer importCitiesListenerContainer(
      final ConnectionFactory connectionFactory, final MessageListenerAdapter importCitiesListenerAdapter) {
    final var container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queues.getImportCitiesQueue());
    container.setMessageListener(importCitiesListenerAdapter);
    container.setDefaultRequeueRejected(false);
    return container;
  }

  @Bean
  public MessageListenerAdapter importCitiesListenerAdapter(final ImportCitiesConsumer consumer) {
    return new MessageListenerAdapter(consumer, "consume");
  }


  @Bean
  public SimpleMessageListenerContainer importHotelsListenerContainer(
      final ConnectionFactory connectionFactory, final MessageListenerAdapter importHotelsListenerAdapter) {
    final var container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queues.getImportHotelsQueue());
    container.setMessageListener(importHotelsListenerAdapter);
    container.setDefaultRequeueRejected(false);
    return container;
  }

  @Bean
  public MessageListenerAdapter importHotelsListenerAdapter(final ImportHotelConsumer consumer) {
    return new MessageListenerAdapter(consumer, "consume");
  }
}
