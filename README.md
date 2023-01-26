## Booking.com Static Data Handling Microservices

[![Build Status](http://iaolg9igjr3tghootxslfe.webrelay.io/api/badges/ujar-org/bs-dst-k8s-bookingdb/status.svg)](http://iaolg9igjr3tghootxslfe.webrelay.io/ujar-org/bs-dst-k8s-bookingdb)

This example shows how to create a microservices architecture and deploy it with Kubernetes.

This project creates a complete microservice demo system in Docker
containers. The services are implemented in Java using Spring and Spring Cloud.

It uses three microservices:
- `init-container-service` to run the database migrations on deploy.
- `edge-service` - provide API gateway, which supports reactive http communications to underlying service (dashboard), simple GraphQL interface to fetch countries, cities & hotels data.
- `importer-service` to handle Booking.com API descriptive data (countries, cities, hotels lists).
- `dashboard-service` provides API for frontend/end user interactions.

### Technology stack

Java 17, Maven 3, Spring Boot 2.7, Spring Cloud 2021.0.3, mysql:5.7.39, rabbitmq:3.8

_Including utils:_ liquibase, WireMock, Mysql testcontainers, docker-compose._dev_.yml,
logbook, micrometer, _checkstyle_ configuration, SpotBugs, PMD etc.

### Pre-requisites

- [Docker](https://docs.docker.com/install/)
- [Minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)
- [Virtualbox](https://www.virtualbox.org/manual/ch02.html)
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- [Helm](https://helm.sh/docs/intro/install/)
- [Apache Maven](https://maven.apache.org/install.html)
- [HTTPie](https://httpie.org/doc#installation)
- [tree](http://mama.indstate.edu/users/ice/tree/)

### Start Kubernetes cluster

```bash
cd ./bs-dst-k8s-bookingdb/scripts/
./start-cluster.sh
```

### Configure Kubernetes cluster

```bash
cd ./bs-dst-k8s-bookingdb/scripts/
./setup-cluster.sh
```

### Deploy application to Kubernetes cluster

```bash
cd ./bs-dst-k8s-bookingdb/scripts/
./install-all.sh
```

### Undeploy application from Kubernetes cluster

```bash
cd ./bs-dst-k8s-bookingdb/scripts/
./delete-all.sh
```

### Delete Application specific Kubernetes cluster configuration (namespaces, clusterRole, etc.)

```bash
cd ./bs-dst-k8s-bookingdb/scripts/
./destroy-cluster.sh
```

### Stop Kubernetes cluster

```bash
cd ./bs-dst-k8s-bookingdb/scripts/
./stop-cluster.sh
```

### Code conventions

The code follows [Google Code Conventions](https://google.github.io/styleguide/javaguide.html). Code
quality is measured by:

- [Sonarqube](https://sonarqube.c2a2.com/dashboard?id=ujar-org%3Abs-dst-k8s-bookingdb)
- [PMD](https://pmd.github.io/)
- [CheckStyle](https://checkstyle.sourceforge.io/)
- [SpotBugs](https://spotbugs.github.io/)

### Tests

This project has standard JUnit tests. To run them execute this command:

```text
./mvnw test -P testcontainers-support
```
