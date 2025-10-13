# Centralized Logging Configuration

This module provides a centralized logging configuration for all microservices in the Sample Booking Demand API ETL project. It ensures consistent logging patterns, structured output, and proper correlation ID management across all services.

## Features

### 🎯 Core Features

- **Centralized Configuration**: Single logback configuration shared across all services
- **Environment-Specific Settings**: Different configurations for dev, staging, and production
- **Structured Logging**: JSON output support for production environments
- **Correlation ID Management**: Automatic correlation ID generation and propagation
- **Performance Monitoring**: Built-in performance logging and slow operation tracking
- **Security Event Logging**: Dedicated security event logging capabilities

### 🔧 Technical Features

- **Async Logging**: Non-blocking async appenders for better performance
- **Rolling File Policy**: Automatic log rotation based on size and time
- **MDC Integration**: Mapped Diagnostic Context for enriched log messages
- **Spring Boot Integration**: Auto-configuration with Spring Boot
- **Distributed Tracing**: Integration with Spring Cloud Sleuth

## Quick Start

### 1. Add Dependency

Add the logging configuration dependency to your service's `pom.xml`:

```xml
<dependency>
    <groupId>com.iqkv</groupId>
    <artifactId>sample-booking-demand-api-etl-logging-configuration</artifactId>
</dependency>
```

### 2. Configure Application Properties

The logging module will automatically configure itself using Spring Boot's auto-configuration. You can customize the behavior using application properties:

```yaml
# Enable structured logging (JSON format)
iqkv:
  logging:
    structured: true
    correlation-id: true
    performance-logging: true
```

### 3. Use in Your Code

```java
import com.iqkv.sample.bookingdemandapietl.logging.CorrelationIdUtils;
import com.iqkv.sample.bookingdemandapietl.logging.StructuredLogging;

@Service
@Slf4j
public class MyService {

  public void businessOperation(String userId) {
    // Set user context
    CorrelationIdUtils.setUserId(userId);

    // Log business event
    StructuredLogging.logBusinessEvent(log, "USER_REGISTRATION", "User registered successfully", Map.of("userId", userId, "source", "web"));

    // Performance logging
    StructuredLogging.loggedOperation(log, "database-query", () -> {
      return performDatabaseQuery();
    });
  }
}
```

## Configuration

### Environment Profiles

#### Development (`application-dev.yml`)

- Colored console output
- Debug level logging for application packages
- File logging enabled
- Extended actuator endpoints

#### Staging (`application-staging.yml`)

- JSON structured logging
- Info level logging
- Performance monitoring enabled
- Testing-friendly actuator endpoints

#### Production (`application-prod.yml`)

- JSON structured logging only
- Warn level for framework logging
- Minimal actuator endpoints
- Optimized for security and performance

### Custom Configuration

You can override default settings using application properties:

```yaml
iqkv:
  logging:
    structured: true # Enable JSON format
    correlation-id: true # Enable correlation IDs
    tracing: true # Enable distributed tracing
    application-log-level: INFO # App package log level
    spring-log-level: WARN # Spring framework log level
    database-log-level: INFO # Database query log level
    performance-logging: true # Enable performance logs
    slow-operation-threshold-ms: 5000 # Slow operation threshold
    security-logging: true # Enable security event logs
    business-event-logging: true # Enable business event logs
    additional-mdc-fields: # Additional MDC fields
      datacenter: "us-east-1"
      version: "${spring.application.version}"

logging:
  file:
    path: "/var/log/app" # Log file directory
    name: "${spring.application.name}" # Log file name
  logback:
    rollingpolicy:
      max-file-size: 100MB # Max file size before rotation
      max-history: 30 # Days to keep old files
      total-size-cap: 1GB # Total size cap for all files
```

## Utility Classes

### CorrelationIdUtils

Manages correlation IDs for request tracing:

```java
// Generate new correlation ID
String correlationId = CorrelationIdUtils.generateCorrelationId();

// Set correlation ID in MDC
CorrelationIdUtils.setCorrelationId(correlationId);

// Get current correlation ID
String currentId = CorrelationIdUtils.getCorrelationId();

// Execute code with correlation ID
CorrelationIdUtils.runWithCorrelationId(correlationId, () -> {
    // Your code here
});

// Set additional context
CorrelationIdUtils.setUserId("user123");
CorrelationIdUtils.setSessionId("session456");
```

### StructuredLogging

Provides structured logging patterns:

```java
// Business event logging
StructuredLogging.logBusinessEvent(
    logger,
    "ORDER_CREATED",
    "New order created",
    Map.of("orderId", "12345", "amount", 99.99)
);

// Performance logging
StructuredLogging.logPerformanceEvent(
    logger,
    "database-query",
    Duration.ofMillis(150),
    Map.of("query", "SELECT * FROM orders")
);

// Technical event logging
StructuredLogging.logTechnicalEvent(
    logger,
    "cache-refresh",
    "redis-cache",
    "Cache refreshed successfully",
    Map.of("cacheSize", 1000)
);

// Security event logging
StructuredLogging.logSecurityEvent(
    logger,
    "FAILED_LOGIN",
    "Failed login attempt",
    Map.of("ip", "192.168.1.1", "username", "admin")
);

// Automatic performance tracking
Result result = StructuredLogging.loggedOperation(logger, "api-call", () -> {
    return callExternalApi();
});
```

## Log Format

### Development Environment

```
2025-01-15 10:30:45.123 [  main] INFO  [trace123,span456] c.i.s.b.service.UserService : Business event: USER_REGISTRATION - User registered successfully
```

### Production Environment

```json
{
  "timestamp": "2025-01-15T10:30:45.123Z",
  "level": "INFO",
  "logger": "com.iqkv.sample.bookingdemandapietl.service.UserService",
  "message": "Business event: USER_REGISTRATION - User registered successfully",
  "service": "dashboard-service",
  "traceId": "trace123",
  "spanId": "span456",
  "correlationId": "corr789",
  "userId": "user123",
  "eventType": "BUSINESS",
  "businessEvent": "USER_REGISTRATION",
  "thread": "http-nio-8080-exec-1",
  "hostname": "app-server-01",
  "environment": "prod"
}
```

## Integration with Monitoring

### Correlation ID Header

The logging module automatically handles the `X-Correlation-ID` header:

- Extracts correlation ID from incoming requests
- Generates new correlation ID if not present
- Adds correlation ID to response headers
- Propagates correlation ID to downstream services

### Spring Cloud Sleuth

Integrates with Spring Cloud Sleuth for distributed tracing:

- Automatic span creation
- Trace and span ID propagation
- Integration with Zipkin/Jaeger

### Actuator Endpoints

Provides enhanced actuator endpoints for monitoring:

- `/actuator/loggers` - Runtime log level management
- `/actuator/logfile` - Access to log files
- `/actuator/metrics` - Performance metrics

## Best Practices

### 1. Use Structured Logging Methods

```java
// Good: Use structured logging
StructuredLogging.logBusinessEvent(log, "USER_LOGIN", "User logged in",
    Map.of("userId", userId, "source", "mobile"));

// Avoid: Plain log messages without context
log.info("User logged in: " + userId);
```

### 2. Set Proper Context

```java
// Set user context at the beginning of operations
CorrelationIdUtils.setUserId(getCurrentUserId());

// Clear context when switching contexts
CorrelationIdUtils.clearAll();
```

### 3. Use Performance Logging

```java
// Wrap expensive operations
return StructuredLogging.loggedOperation(log, "database-query", () -> {
    return repository.findComplexData(parameters);
});
```

### 4. Log Security Events

```java
// Always log security-relevant events
StructuredLogging.logSecurityEvent(log, "UNAUTHORIZED_ACCESS",
    "Unauthorized API access attempt",
    Map.of("ip", request.getRemoteAddr(), "endpoint", request.getRequestURI()));
```

## Troubleshooting

### Common Issues

1. **Correlation ID Not Propagating**
   - Ensure `iqkv.logging.correlation-id=true` in configuration
   - Check if the servlet filter is being registered

2. **JSON Format Not Working**
   - Set `iqkv.logging.structured=true`
   - Verify the profile is set correctly (staging/prod)

3. **Performance Impact**
   - Async logging is enabled by default
   - Adjust log levels in production
   - Monitor disk space for log files

### Log Level Management

Change log levels at runtime using actuator:

```bash
# Get current log levels
curl http://localhost:8080/actuator/loggers

# Change log level
curl -X POST http://localhost:8080/actuator/loggers/com.iqkv.sample.bookingdemandapietl \
  -H 'Content-Type: application/json' \
  -d '{"configuredLevel": "DEBUG"}'
```

## Migration Guide

### From Individual Logback Configurations

1. Remove existing `logback-spring.xml` files from services
2. Add logging-configuration dependency
3. Update application properties if needed
4. Replace manual correlation ID handling with utility classes
5. Migrate to structured logging methods

### Example Migration

**Before:**

```java
@Service
@Slf4j
public class UserService {

  public void createUser(User user) {
    MDC.put("correlationId", UUID.randomUUID().toString());
    log.info("Creating user: {}", user.getId());
    // ... business logic
    MDC.clear();
  }
}
```

**After:**

```java
@Service
@Slf4j
public class UserService {

  public void createUser(User user) {
    StructuredLogging.logBusinessEvent(log, "USER_CREATION", "Creating new user", Map.of("userId", user.getId()));
    // ... business logic (correlation ID handled automatically)
  }
}
```

## Contributing

When extending the logging configuration:

1. Follow the existing patterns and naming conventions
2. Add comprehensive JavaDoc documentation
3. Include unit tests for new functionality
4. Update this README with new features
5. Consider backward compatibility

## Dependencies

- Spring Boot Starter Logging
- Logstash Logback Encoder
- Spring Cloud Sleuth
- SLF4J API
- Logback Classic

## License

This module is part of the Sample Booking Demand API ETL project and is licensed under the Apache License 2.0.
