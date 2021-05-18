################################################################################
# Java Build (JDK+Maven)
################################################################################
FROM adoptopenjdk:15-jdk-hotspot as java-build

## Install maven
RUN apt-get update -yqq && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends maven && \
    rm -rf /var/lib/apt/lists/*

## Create app user
RUN groupadd -g 1000 app && \
    useradd -md /var/lib/app -u 1000 -g 1000 app && \
    mkdir -p /app && \
    chown app:app /app

USER app
WORKDIR /app

################################################################################
# Java Runtime (JRE)
################################################################################
FROM adoptopenjdk/openjdk15:alpine-jre as java

# Fix java DNS resolution issues
RUN echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf

## Create app user
RUN addgroup -g 1000 app && \
    adduser -h /var/lib/app -u 1000 -G app -D app && \
    mkdir -p /app && \
    chown app:app /app
USER app
WORKDIR /app

CMD ["java", "-jar", "app.jar"]

################################################################################
# Mediscreen Build
################################################################################
FROM java-build as mediscreen-build

COPY --chown=app . .
RUN mvn package -DskipTests

################################################################################
# Mediscreen Services
################################################################################

# Patients
FROM java as patients
COPY --chown=app --from=mediscreen-build /app/patients/service/target/mediscreen-patients-service.jar app.jar
EXPOSE 8081

# Notes
FROM java as notes
COPY --chown=app --from=mediscreen-build /app/notes/service/target/mediscreen-notes-service.jar app.jar
EXPOSE 8082

# Assessment
FROM java as assessment
COPY --chown=app --from=mediscreen-build /app/assessment/service/target/mediscreen-assessment-service.jar app.jar
EXPOSE 8083
