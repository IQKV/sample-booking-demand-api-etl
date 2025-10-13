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
import org.slf4j.MDC;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Utility class for managing correlation IDs in distributed systems.
 * Provides methods to generate, set, get, and clear correlation IDs.
 */
@UtilityClass
public class CorrelationIdUtils {

  public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
  public static final String CORRELATION_ID_MDC_KEY = "correlationId";
  public static final String USER_ID_MDC_KEY = "userId";
  public static final String SESSION_ID_MDC_KEY = "sessionId";
  public static final String REQUEST_ID_MDC_KEY = "requestId";

  /**
   * Generates a new correlation ID using UUID.
   *
   * @return a new correlation ID
   */
  public static String generateCorrelationId() {
    return UUID.randomUUID().toString();
  }

  /**
   * Sets the correlation ID in the MDC.
   *
   * @param correlationId the correlation ID to set
   */
  public static void setCorrelationId(String correlationId) {
    if (correlationId != null) {
      MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
    }
  }

  /**
   * Gets the current correlation ID from the MDC.
   *
   * @return the current correlation ID, or null if not set
   */
  public static String getCorrelationId() {
    return MDC.get(CORRELATION_ID_MDC_KEY);
  }

  /**
   * Gets the current correlation ID from the MDC, or generates a new one if not set.
   *
   * @return the current or new correlation ID
   */
  public static String getOrGenerateCorrelationId() {
    String correlationId = getCorrelationId();
    if (correlationId == null) {
      correlationId = generateCorrelationId();
      setCorrelationId(correlationId);
    }
    return correlationId;
  }

  /**
   * Sets the user ID in the MDC for logging purposes.
   *
   * @param userId the user ID to set
   */
  public static void setUserId(String userId) {
    if (userId != null) {
      MDC.put(USER_ID_MDC_KEY, userId);
    }
  }

  /**
   * Gets the current user ID from the MDC.
   *
   * @return the current user ID, or null if not set
   */
  public static String getUserId() {
    return MDC.get(USER_ID_MDC_KEY);
  }

  /**
   * Sets the session ID in the MDC for logging purposes.
   *
   * @param sessionId the session ID to set
   */
  public static void setSessionId(String sessionId) {
    if (sessionId != null) {
      MDC.put(SESSION_ID_MDC_KEY, sessionId);
    }
  }

  /**
   * Gets the current session ID from the MDC.
   *
   * @return the current session ID, or null if not set
   */
  public static String getSessionId() {
    return MDC.get(SESSION_ID_MDC_KEY);
  }

  /**
   * Sets the request ID in the MDC for logging purposes.
   *
   * @param requestId the request ID to set
   */
  public static void setRequestId(String requestId) {
    if (requestId != null) {
      MDC.put(REQUEST_ID_MDC_KEY, requestId);
    }
  }

  /**
   * Gets the current request ID from the MDC.
   *
   * @return the current request ID, or null if not set
   */
  public static String getRequestId() {
    return MDC.get(REQUEST_ID_MDC_KEY);
  }

  /**
   * Clears the correlation ID from the MDC.
   */
  public static void clearCorrelationId() {
    MDC.remove(CORRELATION_ID_MDC_KEY);
  }

  /**
   * Clears the user ID from the MDC.
   */
  public static void clearUserId() {
    MDC.remove(USER_ID_MDC_KEY);
  }

  /**
   * Clears the session ID from the MDC.
   */
  public static void clearSessionId() {
    MDC.remove(SESSION_ID_MDC_KEY);
  }

  /**
   * Clears the request ID from the MDC.
   */
  public static void clearRequestId() {
    MDC.remove(REQUEST_ID_MDC_KEY);
  }

  /**
   * Clears all correlation-related data from the MDC.
   */
  public static void clearAll() {
    clearCorrelationId();
    clearUserId();
    clearSessionId();
    clearRequestId();
  }

  /**
   * Executes a runnable with a correlation ID set in the MDC.
   * The correlation ID is cleared after execution.
   *
   * @param correlationId the correlation ID to set
   * @param runnable      the runnable to execute
   */
  public static void runWithCorrelationId(String correlationId, Runnable runnable) {
    String previousCorrelationId = getCorrelationId();
    try {
      setCorrelationId(correlationId);
      runnable.run();
    } finally {
      if (previousCorrelationId != null) {
        setCorrelationId(previousCorrelationId);
      } else {
        clearCorrelationId();
      }
    }
  }

  /**
   * Executes a supplier with a correlation ID set in the MDC.
   * The correlation ID is cleared after execution.
   *
   * @param correlationId the correlation ID to set
   * @param supplier      the supplier to execute
   * @return the result of the supplier
   */
  public static <T> T runWithCorrelationId(String correlationId, Supplier<T> supplier) {
    String previousCorrelationId = getCorrelationId();
    try {
      setCorrelationId(correlationId);
      return supplier.get();
    } finally {
      if (previousCorrelationId != null) {
        setCorrelationId(previousCorrelationId);
      } else {
        clearCorrelationId();
      }
    }
  }
}