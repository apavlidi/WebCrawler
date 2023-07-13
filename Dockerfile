# AS <NAME> to name this stage as maven
FROM maven:3-openjdk-17 AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app

ENV CODACY_PROJECT_TOKEN=5dd723057df7435281bb69772bad8542

# Compile and package the application to an executable JAR
RUN curl -o get.sh -Ls https://coverage.codacy.com/get.sh && \
    chmod +x get.sh && \
    ./get.sh


RUN mvn package



# For Java 11,
FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=web-crawler-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

# Copy the spring-boot-api-tutorial.jar from the maven stage to the /opt/app directory of the current stage.
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","web-crawler-0.0.1-SNAPSHOT.jar"]