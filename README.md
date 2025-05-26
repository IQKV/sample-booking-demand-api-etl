## 🚀 Booking.com Static Data Handling Microservices

This example shows how to create a microservices architecture and deploy it with Kubernetes.

This project creates a complete microservice demo system in Docker
containers. The services are implemented in Java using Spring and Spring Cloud.

It uses three microservices:

- `init-container-service` to run the database migrations on deploy.
- `edge-service` - provide API gateway, which supports reactive http communications to underlying service (dashboard), simple GraphQL interface to fetch countries, cities & hotels data.
- `importer-service` to handle Booking.com API descriptive data (countries, cities, hotels lists).
- `dashboard-service` provides API for frontend/end user interactions.

### Technology stack

Java 21, Maven 3, Spring Boot, Spring Cloud, mysql:5.7.44, rabbitmq:3.8

_Including utils:_ liquibase, WireMock, Mysql testcontainers, docker-compose._dev_.yml,
_checkstyle_ configuration, SpotBugs, PMD etc.

### Pre-requisites

- [Docker](https://docs.docker.com/install/)
- [Minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)
- [Virtualbox](https://www.virtualbox.org/manual/ch02.html)
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- [Helm](https://helm.sh/docs/intro/install/)
- [Apache Maven](https://maven.apache.org/install.html)
- [HTTPie](https://httpie.org/doc#installation)
- [tree](http://mama.indstate.edu/users/ice/tree/)

### Start Minkkube Kubernetes cluster

```bash
cd ./src/main/minikube/scripts/
./start-cluster.sh
```

### Configure Kubernetes cluster

```bash
cd ./src/main/minikube/scripts/
./setup-cluster.sh
```

### Deploy application to Kubernetes cluster

```bash
cd ./src/main/minikube/scripts/
./install-all.sh
```

### Undeploy application from Kubernetes cluster

```bash
cd ./src/main/minikube/scripts/
./delete-all.sh
```

### Delete Application specific Kubernetes cluster configuration (namespaces, clusterRole, etc.)

```bash
cd ./src/main/minikube/scripts/
./destroy-cluster.sh
```

### Stop Kubernetes cluster

```bash
cd ./src/main/minikube/scripts/
./stop-cluster.sh
```

### Code conventions

The code follows [Google Code Conventions](https://google.github.io/styleguide/javaguide.html). Code
quality is measured by:

- [SonarQube](https://docs.sonarsource.com/)
- [PMD](https://pmd.github.io/)
- [CheckStyle](https://checkstyle.sourceforge.io/)
- [SpotBugs](https://spotbugs.github.io/)
- [Qulice](https://www.qulice.com/)

### Tests

This project contains JUnit tests, Hamcrest matchers, Mockito test doubles, Wiremock stubs, etc. You can run the test suite using

```bash
./mvnw verify -P use-testcontainers
```

> ### Versioning
>
> Project uses a three-segment [CalVer](https://calver.org/) scheme, with a short year in the major version slot, short month in the minor version slot, and micro/patch version in the third
> and final slot.
>
> ```
>  YY.MM.MICRO
> ```
>
> 1. **YY** - short year - 6, 16, 106
> 2. **MM** - short month - 1, 2 ... 11, 12
> 3. **MICRO** - "patch" segment
