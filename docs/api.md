# API Documentation

## Overview

The system provides both REST and GraphQL APIs for accessing Booking.com descriptive data.

## API Gateway (edge-service)

**Base URL**: `http://localhost:8080` (default)

### GraphQL Endpoint

**Endpoint**: `POST /graphql`

#### Schema Location

The GraphQL schema is defined in `edge-service/src/main/resources/graphql/schema.graphqls`

#### Example Queries

**Get All Countries**:

```graphql
{
  countries {
    code
    name
  }
}
```

**Get Cities with Country Information**:

```graphql
{
  cities {
    name
    country {
      code
      name
    }
  }
}
```

**Get Hotels with Location Details**:

```graphql
{
  hotels {
    name
    city {
      name
      country {
        name
      }
    }
  }
}
```

**Filtered Queries**:

```graphql
{
  countries(code: "US") {
    code
    name
    cities {
      name
    }
  }
}
```

#### Using HTTPie

```bash
# Basic countries query
http POST :8080/graphql query='{ countries { code name } }'

# Complex nested query
http POST :8080/graphql query='{
  countries(limit: 5) {
    code
    name
    cities(limit: 3) {
      name
      hotels(limit: 2) {
        name
      }
    }
  }
}'
```

#### Using curl

```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ countries { code name } }"}'
```

## REST API (dashboard-service)

**Base URL**: `http://localhost:8080/api` (when accessed through edge-service)
**Direct URL**: `http://localhost:3000/api` (local development)

### Countries API

#### Get All Countries

```http
GET /api/countries
```

**Response**:

```json
[
  {
    "code": "US",
    "name": "United States"
  },
  {
    "code": "GB",
    "name": "United Kingdom"
  }
]
```

#### Get Country by Code

```http
GET /api/countries/{code}
```

**Example**:

```bash
http GET :8080/api/countries/US
```

### Cities API

#### Get All Cities

```http
GET /api/cities
```

#### Get Cities by Country

```http
GET /api/cities?country={countryCode}
```

**Example**:

```bash
http GET :8080/api/cities country==US
```

### Hotels API

#### Get All Hotels

```http
GET /api/hotels
```

#### Get Hotels by City

```http
GET /api/hotels?city={cityName}
```

**Example**:

```bash
http GET :8080/api/hotels city=="New York"
```

## Data Import API (ingestor-service)

**Base URL**: `http://localhost:8081` (Docker) or `http://localhost:8080` (local)

### Trigger Data Import

#### Import Countries

```http
POST /api/import/countries
```

#### Import Cities

```http
POST /api/import/cities
```

#### Import Hotels

```http
POST /api/import/hotels
```

**Example**:

```bash
# Trigger country import
http POST :8081/api/import/countries

# Check import status
http GET :8081/actuator/health
```

## Monitoring & Management APIs

### Health Checks

#### Application Health

```http
GET /actuator/health
```

**Response**:

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "rabbit": {
      "status": "UP"
    }
  }
}
```

#### Readiness Probe

```http
GET /actuator/health/readiness
```

#### Liveness Probe

```http
GET /actuator/health/liveness
```

### Metrics

#### Prometheus Metrics

```http
GET /actuator/prometheus
```

#### Application Metrics

```http
GET /actuator/metrics
```

#### Specific Metric

```http
GET /actuator/metrics/{metric-name}
```

**Example**:

```bash
# JVM memory usage
http GET :8080/actuator/metrics/jvm.memory.used

# HTTP request metrics
http GET :8080/actuator/metrics/http.server.requests
```

### Application Information

#### Application Info

```http
GET /actuator/info
```

**Response**:

```json
{
  "app": {
    "name": "dashboard-service",
    "description": "Admin REST API Service",
    "version": "0.25.0-SNAPSHOT",
    "git_commit": "abc123",
    "build_time": "2024-01-15T10:30:00Z"
  }
}
```

#### Environment Variables

```http
GET /actuator/env
```

## OpenAPI Documentation

### Swagger UI

- **Dashboard Service**: `http://localhost:3000/swagger-ui.html` (local)
- **Edge Service**: `http://localhost:8080/swagger-ui.html`

### OpenAPI Specification

- **Dashboard Service**: `http://localhost:3000/v3/api-docs`
- **Edge Service**: `http://localhost:8080/v3/api-docs`

## Error Handling

### Standard Error Response

```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Country not found",
  "path": "/api/countries/XX"
}
```

### HTTP Status Codes

- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request parameters
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

### GraphQL Errors

```json
{
  "errors": [
    {
      "message": "Country not found",
      "locations": [{ "line": 2, "column": 3 }],
      "path": ["countries", 0]
    }
  ],
  "data": null
}
```

## Rate Limiting & Security

### Rate Limiting

- Default: 100 requests per minute per IP
- Configurable via application properties

### Authentication

- Currently: No authentication (demo purposes)
- Production: Implement Spring Security with JWT/OAuth2

### CORS Configuration

- Enabled for development
- Configure origins for production deployment

## Testing APIs

### Integration Test Examples

```bash
# Test complete flow
http POST :8081/api/import/countries
sleep 5
http GET :8080/graphql query='{ countries { code name } }'

# Performance testing
for i in {1..100}; do
  http GET :8080/api/countries &
done
wait
```

### Load Testing

```bash
# Using Apache Bench
ab -n 1000 -c 10 http://localhost:8080/api/countries

# Using curl for GraphQL
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ countries { code name } }"}' \
  -w "@curl-format.txt"
```
