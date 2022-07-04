## Booking.com Static Data Handling Microservices 


### Technology stack

Java 17, Maven 3, Spring Boot 2.7, Spring Cloud 2021.0.3, mysql:5.7.34

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
