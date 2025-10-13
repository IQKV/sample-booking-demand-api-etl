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

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auto-configuration for centralized logging features.
 */
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingAutoConfiguration {

  /**
   * Creates a correlation ID filter for web requests.
   * This filter ensures that every request has a correlation ID for tracking.
   */
  @Bean
  @ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
  @ConditionalOnProperty(name = "iqkv.logging.correlation-id", havingValue = "true", matchIfMissing = true)
  @Order(-100) // High precedence to ensure correlation ID is set early
  public CorrelationIdFilter correlationIdFilter() {
    return new CorrelationIdFilter();
  }

  /**
   * Filter to handle correlation ID propagation in HTTP requests.
   */
  public static class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
      try {
        // Check if correlation ID is already present in request header
        String correlationId = request.getHeader(CorrelationIdUtils.CORRELATION_ID_HEADER);
        
        if (correlationId == null || correlationId.trim().isEmpty()) {
          correlationId = CorrelationIdUtils.generateCorrelationId();
        }
        
        // Set correlation ID in MDC for logging
        CorrelationIdUtils.setCorrelationId(correlationId);
        
        // Add correlation ID to response header
        response.setHeader(CorrelationIdUtils.CORRELATION_ID_HEADER, correlationId);
        
        // Extract and set user ID if present
        String userId = extractUserId(request);
        if (userId != null) {
          CorrelationIdUtils.setUserId(userId);
        }
        
        // Extract and set session ID if present
        String sessionId = request.getSession(false) != null ? request.getSession().getId() : null;
        if (sessionId != null) {
          CorrelationIdUtils.setSessionId(sessionId);
        }
        
        // Set request ID (can be same as correlation ID or different)
        CorrelationIdUtils.setRequestId(correlationId);
        
        filterChain.doFilter(request, response);
      } finally {
        // Clean up MDC to prevent memory leaks
        CorrelationIdUtils.clearAll();
      }
    }

    /**
     * Extracts user ID from the request.
     * Override this method to implement custom user ID extraction logic.
     */
    protected String extractUserId(HttpServletRequest request) {
      // Try to get from header first
      String userId = request.getHeader("X-User-ID");
      if (userId != null && !userId.trim().isEmpty()) {
        return userId;
      }
      
      // Try to get from request attribute (set by authentication filter)
      Object userIdAttr = request.getAttribute("userId");
      if (userIdAttr != null) {
        return userIdAttr.toString();
      }
      
      // Try to get from principal if available
      if (request.getUserPrincipal() != null) {
        return request.getUserPrincipal().getName();
      }
      
      return null;
    }
  }
}