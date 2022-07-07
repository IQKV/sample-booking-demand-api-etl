## Booking.com Static Data Handling Microservices

[![Build Status](https://drone.ujar.org/api/badges/ujar-org/micro-k8s-bookingdb/status.svg)](https://drone.ujar.org/ujar-org/micro-k8s-bookingdb)
[![Quality Gate Status](https://sonarqube.ujar.org/api/project_badges/measure?project=ujar-org%3Amicro-k8s-bookingdb&metric=alert_status&token=0c2cbff189a101599b7242832e642390792dc8bf)](https://sonarqube.ujar.org/dashboard?id=ujar-org%3Amicro-k8s-bookingdb)

This example shows how to create a microservices architecture and deploy it with Kubernetes.

This project creates a complete microservice demo system in Docker
containers. The services are implemented in Java using Spring and Spring Cloud.

It uses three microservices:
- `init-container-service` to ran the database migrations on deploy.
- `importer-service` to handle Booking.com API descriptive data (countries, cities, hotels lists).
- `dashboard-service` provides API for frontend/end user interactions.

### Technology stack

Java 17, Maven 3, Spring Boot 2.7, Spring Cloud 2021.0.3, mysql:5.7.34, rabbitmq:3.8

_Including utils:_ liquibase, WireMock, Mysql testcontainers, docker-compose._dev_.yml,
logbook, micrometer, _checkstyle_ configuration, SpotBugs, PMD etc.

### Prerequisites

- Install Docker [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/) - at least 1.6.0
- Add new version of docker-compose [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)
- Spin-up single instance of MySQL by running command:

```
docker-compose -f docker-compose.dev.yml up -d
```

### Code conventions

The code follows [Google Code Conventions](https://google.github.io/styleguide/javaguide.html) without exceptions. Code
quality is measured by:

- [Sonarqube](https://sonarqube.ujar.org/dashboard?id=ujar-org%3Amicro-oss-acmedepartments)
- [PMD](https://pmd.github.io/)
- [CheckStyle](https://checkstyle.sourceforge.io/)
- [SpotBugs](https://spotbugs.github.io/)

### Tests

This project has standard JUnit tests. To run them execute this command:

```text
./mvnw test -P testcontainers-support
```
