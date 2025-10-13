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

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Utility class for structured logging patterns.
 * Provides methods for consistent logging across the application.
 */
@UtilityClass
public class StructuredLogging {

  // Common MDC keys
  public static final String EVENT_TYPE_KEY = "eventType";
  public static final String OPERATION_KEY = "operation";
  public static final String COMPONENT_KEY = "component";
  public static final String DURATION_MS_KEY = "durationMs";
  public static final String STATUS_KEY = "status";
  public static final String ERROR_CODE_KEY = "errorCode";
  public static final String ERROR_MESSAGE_KEY = "errorMessage";
  public static final String BUSINESS_EVENT_KEY = "businessEvent";
  public static final String PERFORMANCE_KEY = "performance";
  public static final String SECURITY_EVENT_KEY = "securityEvent";

  // Event types
  public static final String EVENT_TYPE_BUSINESS = "BUSINESS";
  public static final String EVENT_TYPE_TECHNICAL = "TECHNICAL";
  public static final String EVENT_TYPE_SECURITY = "SECURITY";
  public static final String EVENT_TYPE_PERFORMANCE = "PERFORMANCE";

  // Status values
  public static final String STATUS_SUCCESS = "SUCCESS";
  public static final String STATUS_FAILED = "FAILED";
  public static final String STATUS_ERROR = "ERROR";

  /**
   * Logs a business event with structured information.
   *
   * @param logger      the logger to use
   * @param eventName   the name of the business event
   * @param description the description of the event
   * @param attributes  additional attributes to include
   */
  public static void logBusinessEvent(Logger logger, String eventName, String description, 
                                    Map<String, Object> attributes) {
    withMdc(Map.of(
        EVENT_TYPE_KEY, EVENT_TYPE_BUSINESS,
        BUSINESS_EVENT_KEY, eventName
    ), () -> {
      withAttributes(attributes, () -> {
        logger.info("Business event: {} - {}", eventName, description);
      });
    });
  }

  /**
   * Logs a technical event with structured information.
   *
   * @param logger      the logger to use
   * @param operation   the technical operation name
   * @param component   the component performing the operation
   * @param description the description of the event
   * @param attributes  additional attributes to include
   */
  public static void logTechnicalEvent(Logger logger, String operation, String component, 
                                     String description, Map<String, Object> attributes) {
    withMdc(Map.of(
        EVENT_TYPE_KEY, EVENT_TYPE_TECHNICAL,
        OPERATION_KEY, operation,
        COMPONENT_KEY, component
    ), () -> {
      withAttributes(attributes, () -> {
        logger.info("Technical event: {} in {} - {}", operation, component, description);
      });
    });
  }

  /**
   * Logs a security event with structured information.
   *
   * @param logger      the logger to use
   * @param eventName   the name of the security event
   * @param description the description of the event
   * @param attributes  additional attributes to include
   */
  public static void logSecurityEvent(Logger logger, String eventName, String description, 
                                    Map<String, Object> attributes) {
    withMdc(Map.of(
        EVENT_TYPE_KEY, EVENT_TYPE_SECURITY,
        SECURITY_EVENT_KEY, eventName
    ), () -> {
      withAttributes(attributes, () -> {
        logger.warn("Security event: {} - {}", eventName, description);
      });
    });
  }

  /**
   * Logs a performance event with timing information.
   *
   * @param logger      the logger to use
   * @param operation   the operation that was measured
   * @param duration    the duration of the operation
   * @param attributes  additional attributes to include
   */
  public static void logPerformanceEvent(Logger logger, String operation, Duration duration, 
                                       Map<String, Object> attributes) {
    withMdc(Map.of(
        EVENT_TYPE_KEY, EVENT_TYPE_PERFORMANCE,
        OPERATION_KEY, operation,
        PERFORMANCE_KEY, "timing",
        DURATION_MS_KEY, String.valueOf(duration.toMillis())
    ), () -> {
      withAttributes(attributes, () -> {
        logger.info("Performance: {} completed in {}ms", operation, duration.toMillis());
      });
    });
  }

  /**
   * Executes an operation and logs its performance.
   *
   * @param logger    the logger to use
   * @param operation the name of the operation
   * @param supplier  the operation to execute
   * @return the result of the operation
   */
  public static <T> T loggedOperation(Logger logger, String operation, Supplier<T> supplier) {
    return loggedOperation(logger, operation, Map.of(), supplier);
  }

  /**
   * Executes an operation and logs its performance with additional attributes.
   *
   * @param logger     the logger to use
   * @param operation  the name of the operation
   * @param attributes additional attributes to include
   * @param supplier   the operation to execute
   * @return the result of the operation
   */
  public static <T> T loggedOperation(Logger logger, String operation, Map<String, Object> attributes, 
                                    Supplier<T> supplier) {
    Instant start = Instant.now();
    try {
      logger.debug("Starting operation: {}", operation);
      T result = supplier.get();
      Duration duration = Duration.between(start, Instant.now());
      
      withMdc(Map.of(
          OPERATION_KEY, operation,
          DURATION_MS_KEY, String.valueOf(duration.toMillis()),
          STATUS_KEY, STATUS_SUCCESS
      ), () -> {
        withAttributes(attributes, () -> {
          logger.info("Operation {} completed successfully in {}ms", operation, duration.toMillis());
        });
      });
      
      return result;
    } catch (Exception e) {
      Duration duration = Duration.between(start, Instant.now());
      
      withMdc(Map.of(
          OPERATION_KEY, operation,
          DURATION_MS_KEY, String.valueOf(duration.toMillis()),
          STATUS_KEY, STATUS_ERROR,
          ERROR_CODE_KEY, e.getClass().getSimpleName(),
          ERROR_MESSAGE_KEY, e.getMessage() != null ? e.getMessage() : "Unknown error"
      ), () -> {
        withAttributes(attributes, () -> {
          logger.error("Operation {} failed after {}ms: {}", operation, duration.toMillis(), e.getMessage(), e);
        });
      });
      
      throw e;
    }
  }

  /**
   * Logs an error with structured information.
   *
   * @param logger      the logger to use
   * @param operation   the operation that failed
   * @param error       the error that occurred
   * @param attributes  additional attributes to include
   */
  public static void logError(Logger logger, String operation, Throwable error, 
                            Map<String, Object> attributes) {
    withMdc(Map.of(
        OPERATION_KEY, operation,
        STATUS_KEY, STATUS_ERROR,
        ERROR_CODE_KEY, error.getClass().getSimpleName(),
        ERROR_MESSAGE_KEY, error.getMessage() != null ? error.getMessage() : "Unknown error"
    ), () -> {
      withAttributes(attributes, () -> {
        logger.error("Error in operation {}: {}", operation, error.getMessage(), error);
      });
    });
  }

  /**
   * Executes a runnable with additional MDC context.
   *
   * @param mdcData the MDC data to set
   * @param runnable the runnable to execute
   */
  public static void withMdc(Map<String, String> mdcData, Runnable runnable) {
    Map<String, String> previousMdc = MDC.getCopyOfContextMap();
    try {
      mdcData.forEach(MDC::put);
      runnable.run();
    } finally {
      MDC.clear();
      if (previousMdc != null) {
        MDC.setContextMap(previousMdc);
      }
    }
  }

  /**
   * Executes a runnable with additional attributes in the MDC.
   *
   * @param attributes the attributes to add to MDC
   * @param runnable   the runnable to execute
   */
  private static void withAttributes(Map<String, Object> attributes, Runnable runnable) {
    if (attributes == null || attributes.isEmpty()) {
      runnable.run();
      return;
    }

    Map<String, String> stringAttributes = attributes.entrySet().stream()
        .collect(java.util.stream.Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue() != null ? entry.getValue().toString() : "null"
        ));

    Map<String, String> previousMdc = MDC.getCopyOfContextMap();
    try {
      stringAttributes.forEach(MDC::put);
      runnable.run();
    } finally {
      stringAttributes.keySet().forEach(MDC::remove);
    }
  }
}