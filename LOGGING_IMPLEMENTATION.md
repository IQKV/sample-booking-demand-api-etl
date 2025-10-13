# Centralized Logging Implementation

This document describes the centralized logging implementation for the Sample Booking Demand API ETL project.

## ✅ Implementation Summary

The centralized logging system has been successfully implemented with the following components:

### 🏗️ Architecture

```
sample-booking-demand-api-etl/
├── booking-demand-api-etl-support/
│   └── logging-configuration/           # New centralized logging module
│       ├── src/main/java/com/iqkv/sample/bookingdemandapietl/logging/
│       │   ├── LoggingProperties.java   # Configuration properties
│       │   ├── CorrelationIdUtils.java  # Correlation ID management
│       │   ├── StructuredLogging.java   # Structured logging utilities
│       │   └── LoggingAutoConfiguration.java  # Spring Boot auto-config
│       ├── src/main/resources/
│       │   ├── logback-spring.xml       # Centralized logback config
│       │   ├── application-dev.yml      # Dev environment settings
│       │   ├── application-staging.yml  # Staging environment settings
│       │   ├── application-prod.yml     # Production environment settings
│       │   └── META-INF/spring.factories # Auto-configuration metadata
│       ├── pom.xml                      # Module dependencies
│       └── README.md                    # Comprehensive documentation
├── dashboard-service/                   # Updated to use centralized logging
├── edge-service/                        # Updated to use centralized logging
├── ingestor-service/                    # Updated to use centralized logging
├── init-container-service/              # Updated to use centralized logging
└── pom.xml                             # Updated with logging dependencies
```

### 🎯 Features Implemented

#### Core Logging Features

- ✅ **Centralized Configuration**: Single `logback-spring.xml` shared across all services
- ✅ **Environment-Specific Settings**: Different configurations for dev, staging, and production
- ✅ **Structured Logging**: JSON output for production environments
- ✅ **Async Logging**: Non-blocking async appenders for better performance
- ✅ **Rolling File Policy**: Automatic log rotation based on size and time

#### Correlation & Tracing

- ✅ **Correlation ID Management**: Automatic generation and propagation
- ✅ **HTTP Header Integration**: `X-Correlation-ID` header handling
- ✅ **MDC Integration**: Mapped Diagnostic Context for enriched logs
- ✅ **Distributed Tracing**: Spring Cloud Sleuth integration
- ✅ **User Context**: User ID, session ID, and request ID tracking

#### Utility Classes

- ✅ **CorrelationIdUtils**: Complete correlation ID management
- ✅ **StructuredLogging**: Business, technical, security, and performance event logging
- ✅ **LoggingProperties**: Externalized configuration properties
- ✅ **Auto-Configuration**: Spring Boot auto-configuration with servlet filter

#### Service Integration

- ✅ **Dashboard Service**: Added dependency and removed duplicate logback config
- ✅ **Edge Service**: Added dependency and removed duplicate logback config
- ✅ **Ingestor Service**: Added dependency and removed duplicate logback config
- ✅ **Init Container Service**: Added dependency and removed duplicate logback config
- ✅ **Parent POM**: Updated with centralized dependency management

## 🚀 Usage Examples

### Basic Logging

```java
@Service
@Slf4j
public class UserService {

  public User createUser(CreateUserRequest request) {
    // Correlation ID is automatically handled by servlet filter

    // Log business event
    StructuredLogging.logBusinessEvent(log, "USER_CREATION_STARTED", "Starting user creation process", Map.of("email", request.getEmail(), "source", "api"));

    try {
      // Wrap database operation with performance logging
      return StructuredLogging.loggedOperation(log, "create-user-db", () -> {
        User user = new User(request.getEmail(), request.getName());
        return userRepository.save(user);
      });
    } catch (Exception e) {
      StructuredLogging.logError(log, "create-user", e, Map.of("email", request.getEmail()));
      throw e;
    }
  }
}
```

### Web Controller with Context

```java
@RestController
@Slf4j
public class UserController {

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request, HttpServletRequest httpRequest) {
    // Set additional context (correlation ID already handled by filter)
    CorrelationIdUtils.setUserId(getCurrentUserId(httpRequest));

    // Log security-relevant events
    StructuredLogging.logSecurityEvent(log, "API_ACCESS", "User creation API accessed", Map.of("ip", httpRequest.getRemoteAddr(), "endpoint", "/users", "method", "POST"));

    User user = userService.createUser(request);
    return ResponseEntity.ok(user);
  }
}
```

### Async Operations

```java
@Service
@Slf4j
public class NotificationService {

  @Async
  public void sendNotificationAsync(String userId, String message) {
    // Preserve correlation ID across async boundaries
    String correlationId = CorrelationIdUtils.getOrGenerateCorrelationId();

    CorrelationIdUtils.runWithCorrelationId(correlationId, () -> {
      StructuredLogging.logTechnicalEvent(log, "send-notification", "notification-service", "Sending async notification", Map.of("userId", userId, "messageType", "email"));

      // Send notification logic
      emailService.sendEmail(userId, message);
    });
  }
}
```

## 📊 Log Output Examples

### Development Environment (Colored Console)

```
2025-10-13 14:32:15.123 [http-nio-8080-exec-1] INFO  [a1b2c3d4,e5f6g7h8] c.i.s.b.s.UserService : Business event: USER_CREATION_STARTED - Starting user creation process
2025-10-13 14:32:15.156 [http-nio-8080-exec-1] INFO  [a1b2c3d4,e5f6g7h8] c.i.s.b.s.UserService : Operation create-user-db completed successfully in 33ms
```

### Production Environment (JSON)

```json
{
  "timestamp": "2025-10-13T14:32:15.123Z",
  "level": "INFO",
  "logger": "com.iqkv.sample.bookingdemandapietl.service.UserService",
  "message": "Business event: USER_CREATION_STARTED - Starting user creation process",
  "service": "dashboard-service",
  "traceId": "a1b2c3d4",
  "spanId": "e5f6g7h8",
  "correlationId": "corr-123-456",
  "userId": "user789",
  "eventType": "BUSINESS",
  "businessEvent": "USER_CREATION_STARTED",
  "email": "user@example.com",
  "source": "api",
  "thread": "http-nio-8080-exec-1",
  "hostname": "dashboard-pod-1",
  "environment": "prod"
}
```

## 🔧 Configuration

### Environment-Specific Settings

Each environment has optimized logging configuration:

- **Development**: Colored console output, DEBUG level, extended actuator endpoints
- **Staging**: JSON format, DEBUG level for testing, extended actuator endpoints
- **Production**: JSON format only, INFO level, minimal actuator endpoints, security-focused

### Runtime Configuration

Log levels can be changed at runtime:

```bash
# Check current levels
curl http://localhost:8080/actuator/loggers

# Change application log level
curl -X POST http://localhost:8080/actuator/loggers/com.iqkv.sample.bookingdemandapietl \
  -H 'Content-Type: application/json' \
  -d '{"configuredLevel": "DEBUG"}'
```

## 🏃‍♂️ Running the Implementation

### 1. Build the Project

```bash
mvn clean install
```

### 2. Run a Service with Development Profile

```bash
cd dashboard-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Run with Production Profile

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 4. Test Correlation ID Propagation

```bash
# Send request with correlation ID
curl -H "X-Correlation-ID: test-correlation-123" \
     http://localhost:8080/api/endpoint

# Check logs - should show correlation ID in all log entries
```

## 🛠️ Migration Status

### ✅ Completed

- Created centralized logging configuration module
- Implemented correlation ID utilities and structured logging
- Added environment-specific configurations
- Updated all service modules to use centralized logging
- Removed duplicate logback configurations
- Updated parent POM with dependency management
- Created comprehensive documentation

### 🔄 Recommendations for Future Enhancements

1. **Log Aggregation**: Integrate with ELK Stack or Splunk
2. **Metrics Integration**: Add custom metrics for log events
3. **Alert Configuration**: Set up alerts for error patterns
4. **Performance Monitoring**: Add APM integration (New Relic, Datadog)
5. **Log Sampling**: Implement sampling for high-volume environments

## 📈 Benefits Achieved

1. **Consistency**: All services now use identical logging patterns
2. **Observability**: Enhanced traceability with correlation IDs and structured data
3. **Maintainability**: Single point of configuration management
4. **Performance**: Async logging reduces I/O blocking
5. **Monitoring**: Better integration with monitoring and alerting systems
6. **Debugging**: Improved troubleshooting capabilities across services

## 🔍 Testing the Implementation

### Manual Testing

1. Start any service with different profiles
2. Send requests and verify log formats
3. Check correlation ID propagation
4. Test actuator endpoints for log level management

### Verification Checklist

- [ ] Logs appear in console/files as expected
- [ ] Correlation IDs are generated and propagated
- [ ] JSON format works in staging/prod profiles
- [ ] Actuator endpoints are accessible
- [ ] Log rotation works correctly
- [ ] No duplicate configurations remain

The centralized logging implementation is now complete and ready for use across all microservices in the project!
