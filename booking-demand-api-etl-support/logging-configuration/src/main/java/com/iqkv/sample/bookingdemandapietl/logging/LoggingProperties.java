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

package com.iqkv.sample.bookingdemandapietl.logging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for centralized logging.
 */
@Data
@ConfigurationProperties(prefix = "iqkv.logging")
public class LoggingProperties {

  /**
   * Enable structured logging (JSON format).
   */
  private boolean structured = false;

  /**
   * Enable correlation ID generation and propagation.
   */
  private boolean correlationId = true;

  /**
   * Enable distributed tracing.
   */
  private boolean tracing = true;

  /**
   * Log level for application packages.
   */
  private String applicationLogLevel = "DEBUG";

  /**
   * Log level for Spring framework.
   */
  private String springLogLevel = "INFO";

  /**
   * Log level for database operations.
   */
  private String databaseLogLevel = "WARN";

  /**
   * Enable performance logging for slow operations.
   */
  private boolean performanceLogging = true;

  /**
   * Threshold for slow operations in milliseconds.
   */
  private long slowOperationThresholdMs = 5000;

  /**
   * Enable security event logging.
   */
  private boolean securityLogging = true;

  /**
   * Enable business event logging.
   */
  private boolean businessEventLogging = true;

  /**
   * Additional MDC fields to include in all log messages.
   */
  private java.util.Map<String, String> additionalMdcFields = new java.util.HashMap<>();
}