FROM maven:3-openjdk-17 AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN mvn package


FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=web-crawler-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","web-crawler-0.0.1-SNAPSHOT.jar"]