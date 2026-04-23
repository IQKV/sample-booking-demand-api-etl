# Development Guide

## Prerequisites

### Required Tools

- **Java 25+**: OpenJDK or Oracle JDK
- **Maven 3.8+**: Build and dependency management
- **Docker & Docker Compose**: Container runtime
- **Git**: Version control

### Optional Tools

- **HTTPie** or **curl**: API testing
- **Minikube**: Local Kubernetes development
- **kubectl**: Kubernetes CLI
- **Helm**: Kubernetes package manager

## Project Structure

```
sample-booking-demand-api-etl/
├── booking-demand-api-etl-support/     # Shared libraries
│   ├── batch-job/                      # Batch processing components
│   ├── booking-api-client/             # External API clients
│   └── booking-persistence/            # Data access layer
├── dashboard-service/                  # Backend REST API
├── edge-service/                       # API Gateway & GraphQL
├── ingestor-service/                   # Data ingestion service
├── init-container-service/             # Database migration service
├── docker/                             # Docker configurations
├── minikube/                           # Kubernetes manifests
└── .devcontainer/                      # VS Code dev container
```

## Local Development Setup

### 1. Infrastructure Setup

Start required infrastructure services:

```bash
# Start MySQL and RabbitMQ
docker compose up -d mysql rabbitmq

# Verify services are running
docker compose ps
```

### 2. Database Migration

Run database migrations:

```bash
./mvnw -pl init-container-service spring-boot:run -Dspring-boot.run.profiles=local
```

### 3. Service Development

#### Build All Services

```bash
./mvnw clean package -DskipTests
```

#### Run Individual Services

**Dashboard Service** (Backend API):

```bash
./mvnw -pl dashboard-service spring-boot:run -Dspring-boot.run.profiles=local
```

**Edge Service** (API Gateway):

```bash
./mvnw -pl edge-service spring-boot:run -Dspring-boot.run.profiles=local
```

**Ingestor Service** (Data Ingestion):

```bash
./mvnw -pl ingestor-service spring-boot:run -Dspring-boot.run.profiles=local
```

### 4. Development Profiles

#### Local Profile (`application-local.yml`)

- Enhanced logging and debugging
- Hot reload enabled
- All actuator endpoints exposed
- SQL logging enabled

#### Configuration Override

```bash
# Custom port
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=3001"

# Custom database
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:mysql://localhost:3307/custom_db"
```

## Testing

### Unit Tests

```bash
# Run all tests
./mvnw test

# Run tests for specific service
./mvnw -pl dashboard-service test
```

### Integration Tests with Testcontainers

```bash
./mvnw verify -P use-testcontainers
```

### API Testing

#### REST API Testing

```bash
# Health check
http GET :8080/actuator/health

# Dashboard API
http GET :8080/api/countries

# Metrics
http GET :8080/actuator/prometheus
```

#### GraphQL Testing

```bash
# Countries query
http POST :8080/graphql query='{ countries { code name } }'

# Cities query
http POST :8080/graphql query='{ cities { name country { name } } }'
```

## Code Quality

### Quality Checks

```bash
# Run all quality checks
./mvnw verify -P use-qulice

# Individual tools
./mvnw checkstyle:check
./mvnw spotbugs:check
./mvnw pmd:check
```

### Code Formatting

```bash
# Check formatting
pnpm formatter:check

# Fix formatting
pnpm formatter:write

# Lint styles
pnpm lint:stylelint
```

## Debugging

### Application Debugging

```bash
# Enable debug mode
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### Database Debugging

- **MySQL Console**: `docker exec -it dev-booking-demand-api-etl-mysql mysql -u svc_testing -p`
- **RabbitMQ Management**: http://localhost:15672 (svc_testing/svc_testing)

### Monitoring

- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/changeme)

## Common Development Tasks

### Adding New Endpoints

1. Create controller in appropriate service
2. Add service layer logic
3. Update OpenAPI documentation
4. Add integration tests
5. Update GraphQL schema (if applicable)

### Database Changes

1. Create Liquibase changeset in `init-container-service`
2. Update JPA entities
3. Run migration locally
4. Test with integration tests

### Adding Dependencies

1. Update parent POM for version management
2. Add dependency to service POM
3. Update documentation if needed

## Troubleshooting

### Common Issues

**Port Conflicts**:

```bash
# Check port usage
netstat -tulpn | grep :8080

# Use different port
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

**Database Connection Issues**:

```bash
# Verify MySQL is running
docker compose ps mysql

# Check connection
mysql -h localhost -P 3306 -u svc_testing -p
```

**Memory Issues**:

```bash
# Increase JVM memory
export MAVEN_OPTS="-Xmx2g -Xms1g"
```

### Logs Location

- **Application Logs**: Console output (structured JSON in production)
- **Docker Logs**: `docker compose logs [service-name]`
- **Kubernetes Logs**: `kubectl logs [pod-name]`
