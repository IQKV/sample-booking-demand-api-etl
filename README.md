## 🚀 Booking.com Static Data Handling Microservices

A production-like microservices demo that ingests, stores, and exposes Booking.com descriptive data. It showcases Spring-based services, message-driven import flows, an API gateway with GraphQL, and turnkey deployments to Docker or Minikube/Kubernetes.

### What you get

- **End-to-end system**: database migrations, ingestion, APIs, gateway
- **Multiple runtimes**: Docker Compose and Minikube/Kubernetes
- **Modern stack**: Java 21, Spring Boot, Spring Cloud, RabbitMQ, MySQL
- **Batteries included**: Testcontainers, WireMock, code quality tools, and scripts

## 📚 Documentation

### Core Documentation

- **[Architecture Overview](docs/architecture.md)** - System design, service interactions, and architectural patterns
- **[API Documentation](docs/api.md)** - REST and GraphQL API reference with examples
- **[Deployment Guide](docs/deployment.md)** - Docker Compose and Kubernetes deployment instructions
- **[Development Guide](docs/development.md)** - Local development setup and best practices

### Project Information

- **[Contributing Guidelines](.github/CONTRIBUTING.md)** - How to contribute to the project
- **[Commit Conventions](.github/COMMIT_CONVENTION.md)** - Git commit message standards
- **[Changelog](CHANGELOG.md)** - Project version history and changes

### Services

- **init-container-service**: Applies Liquibase migrations during deployment.
- **ingestor-service**: Imports Booking.com descriptive data (countries, cities, hotels) and publishes/consumes messages.
- **dashboard-service**: Backend API for UI/end-user interactions.
- **edge-service**: API gateway with reactive HTTP to downstream services and a GraphQL interface for querying countries, cities, and hotels.

### Architecture (high level)

- Client → `edge-service` (REST/GraphQL)
- `edge-service` → `dashboard-service` (HTTP)
- `ingestor-service` ↔ RabbitMQ (asynchronous ingestion pipeline)
- `init-container-service` → MySQL (Liquibase migrations on deploy)
- Shared MySQL database for descriptive data

### Technology stack

Java 21, Maven 3, Spring Boot, Spring Cloud, MySQL 5.7.44, RabbitMQ 3.8

Including utilities: Liquibase, WireMock, MySQL Testcontainers, Docker Compose, Checkstyle, SpotBugs, PMD, Qulice.

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose V2](https://docs.docker.com/compose/) (bundled with recent Docker Desktop)
- [Apache Maven](https://maven.apache.org/install.html)
- [HTTPie](https://httpie.io/docs/cli) or `curl`
- Optional for Kubernetes:
  - [Minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)
  - [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
  - [Helm](https://helm.sh/docs/intro/install/)
  - A VM driver (e.g., VirtualBox)

## Quickstart

### Option A: Run with Docker Compose

From the repository root:

```shell script
docker compose up -d
# or
docker-compose up -d
```

To stop and remove containers:

```shell script
docker compose down -v
```

### Option B: Run on Minikube/Kubernetes

Use the helper scripts in `minikube/scripts/`.

```shell script
cd minikube/scripts/

# Start Minikube
./start-cluster.sh

# Configure namespaces, RBAC, etc.
./setup-cluster.sh

# Deploy all app and infra components
./install-all.sh

# Check status
kubectl get pods -A | cat

# Undeploy app components
./delete-all.sh

# Remove app-specific cluster configuration
./destroy-cluster.sh

# Stop the cluster
./stop-cluster.sh
```

## Local development

Build everything:

```shell script
./mvnw -q -DskipTests package
```

Run individual services (examples):

```shell script
# Edge (GraphQL/REST gateway)
./mvnw -pl edge-service spring-boot:run -Dspring-boot.run.profiles=local

# Ingestor (ingestion pipeline)
./mvnw -pl ingestor-service spring-boot:run -Dspring-boot.run.profiles=local

# Dashboard (backend API)
./mvnw -pl dashboard-service spring-boot:run -Dspring-boot.run.profiles=local

# Init container (applies Liquibase migrations)
./mvnw -pl init-container-service spring-boot:run -Dspring-boot.run.profiles=local
```

Configuration lives in each service under `src/main/resources/application.yml` (and `application-local.yml`). Override via environment variables or `-Dspring-boot.run.arguments` as needed.

## GraphQL endpoint

The GraphQL schema is located at `edge-service/src/main/resources/graphql/schema.graphqls`.

Example query with HTTPie (adjust URL/port to your runtime):

```shell script
http POST :8080/graphql query='{ countries { code name } }'
```

## Observability

Provisioning for Prometheus and Grafana is available under `docker/`.

- Prometheus config: `docker/prometheus/prometheus.yml`
- Grafana dashboards and data sources: `docker/grafana/provisioning/`

You can integrate or extend these when running via Docker or Kubernetes according to your environment.

## Code conventions and quality

The code adheres to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Quality checks include:

- [SonarQube](https://docs.sonarsource.com/)
- [PMD](https://pmd.github.io/)
- [Checkstyle](https://checkstyle.sourceforge.io/)
- [SpotBugs](https://spotbugs.github.io/)
- [Qulice](https://www.qulice.com/)

## Testing

JUnit, Hamcrest, Mockito, WireMock, and Testcontainers are used.

```shell script
./mvnw verify -P use-testcontainers
```

## Troubleshooting

- **Minikube resources**: If pods are Pending or OOMKilled, increase CPU/RAM for Minikube.
- **kubectl context**: Ensure your context points to the Minikube cluster: `kubectl config current-context`.
- **Docker port conflicts**: Stop other services occupying database or message broker ports before `docker compose up`.
- **Maven downloads are slow**: Use the Maven wrapper and consider a local repository cache.
- **Profiles**: Use `-Dspring-boot.run.profiles=local` for local runs; Kubernetes manifests supply required env vars for cluster runs.

## Contributing

Contributions are welcome! Please follow Conventional Commits and ensure the build and tests pass before submitting PRs.

## License

This project is licensed under the terms of the [Apache License](LICENSE).
