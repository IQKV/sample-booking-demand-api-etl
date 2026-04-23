# Architecture Overview

## System Architecture

This project demonstrates a production-ready microservices architecture using Spring Boot and Spring Cloud, designed to handle Booking.com descriptive data ingestion and exposure.

### Service Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client Apps   │───▶│   edge-service   │───▶│dashboard-service│
│  (REST/GraphQL) │    │  (API Gateway)   │    │  (Backend API)  │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │                        │
                                │                        ▼
                                │                ┌─────────────────┐
                                │                │     MySQL       │
                                │                │   (Shared DB)   │
                                │                └─────────────────┘
                                │                        ▲
                                ▼                        │
                       ┌──────────────────┐             │
                       │  ingestor-service│─────────────┘
                       │ (Data Ingestion) │
                       └──────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │    RabbitMQ      │
                       │ (Message Broker) │
                       └──────────────────┘
```

### Service Responsibilities

#### edge-service (API Gateway)

- **Technology**: Spring Cloud Gateway, GraphQL
- **Port**: 8080 (default)
- **Purpose**:
  - Single entry point for all client requests
  - Reactive HTTP routing to downstream services
  - GraphQL interface for querying countries, cities, and hotels
  - Request/response transformation and aggregation

#### dashboard-service (Backend API)

- **Technology**: Spring Boot, Spring Data JPA, Spring Web
- **Port**: 8080 (configurable)
- **Purpose**:
  - RESTful API for UI/admin interactions
  - Business logic processing
  - Direct database access for CRUD operations
  - Integration with message queues for async operations

#### ingestor-service (Data Ingestion)

- **Technology**: Spring Boot, Spring Batch, RabbitMQ
- **Port**: 8080 (configurable)
- **Purpose**:
  - Ingests Booking.com descriptive data
  - Publishes and consumes messages for async processing
  - Batch processing of large datasets
  - Data transformation and validation

#### init-container-service (Database Migration)

- **Technology**: Spring Boot, Liquibase
- **Purpose**:
  - Applies database schema migrations on deployment
  - Ensures database consistency across environments
  - Runs as init container in Kubernetes deployments

### Data Flow

1. **Data Ingestion**: `ingestor-service` fetches data from Booking.com APIs
2. **Message Processing**: Async processing via RabbitMQ queues
3. **Data Storage**: Processed data stored in shared MySQL database
4. **API Access**: Clients access data through `edge-service` GraphQL/REST endpoints
5. **Admin Operations**: Administrative tasks handled by `dashboard-service`

### Technology Stack

- **Runtime**: Java 25, Spring Boot 4.x
- **Build**: Maven 3
- **Database**: MySQL 5.7.44
- **Message Broker**: RabbitMQ 3.8
- **API Gateway**: Spring Cloud Gateway
- **API Documentation**: SpringDoc OpenAPI
- **Monitoring**: Spring Boot Actuator, Prometheus, Grafana
- **Testing**: JUnit 5, Testcontainers, WireMock
- **Code Quality**: Checkstyle, SpotBugs, PMD, Qulice

### Deployment Options

1. **Docker Compose**: Single-machine deployment with all services
2. **Kubernetes/Minikube**: Container orchestration with scaling capabilities
3. **Local Development**: Individual service execution with shared infrastructure

### Configuration Management

- **Profiles**: `local`, `dev`, `prod` for environment-specific configurations
- **External Configuration**: Environment variables for sensitive data
- **Feature Flags**: Profile-based feature toggling
