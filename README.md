# [![mediscreen](.readme/logo.png?raw=true)](https://github.com/np111/P9_mediscreen)

### \[ <ins>[backend](https://github.com/np111/P9_mediscreen#readme)</ins> | [webapp](https://github.com/np111/P9_mediscreen-webapp#readme) \]

Mediscreen is an application used to track disease risks.

![Poster](.readme/poster.png?raw=true)

## Documentation

- [HTTP API Documentation](https://np111.github.io/P9_mediscreen/index.html)

## Getting started

These instructions will get you a copy of the project up and running on your
local machine **for development**.

### Prerequisites

- Install [Maven 3.6+](https://maven.apache.org/download.cgi)
- Install
  [Java 11+](https://adoptopenjdk.net/?variant=openjdk15&jvmVariant=hotspot)
- Install [Docker](https://docs.docker.com/get-docker/)
  and [Docker Compose](https://docs.docker.com/compose/install/)

### Running services

Compile the application using maven:

```bash
mvn package
```

Start the development environment (databases):

```bash
./dev.sh docker up
```

Then start all services (each in a different terminal):

```bash
java -jar patients/service/target/mediscreen-patients-service.jar
java -jar notes/service/target/mediscreen-notes-service.jar
java -jar assessment/service/target/mediscreen-assessment-service.jar
```

### Testing

Unit tests:

```bash
mvn test
```

Integration & Unit tests (requires the development environment to be running):

```bash
mvn test -DintegrationTests=true
```

### Updating online documentation

```bash
./docs.sh generate
./docs.sh publish
```

## Deployment

Take a look at docker-compose.yml for a deployment **example**. You can test it
by running:

```bash
docker-compose -p mediscreen up --remove-orphans --build
```

## Web Application

You can also checkout the web application (built using React/Next.js):
[P9_mediscreen-webapp](https://github.com/np111/P9_mediscreen-webapp)

## Notes

This is a school project (for OpenClassrooms).

The goal is to create a micro-services backend using an agile methodology.
