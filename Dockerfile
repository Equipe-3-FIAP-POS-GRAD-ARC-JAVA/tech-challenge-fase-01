# syntax=docker/dockerfile:1.4
ARG JDK_VERSION=21

# TODO: ADD USER

####################
# Build stage
####################
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml ./

COPY .mvn .mvn

COPY src ./src

RUN mvn -B -DskipTests package

####################
# Runtime stage (Temurin 21 JRE)
####################
FROM eclipse-temurin:21-jre-noble AS runtime

WORKDIR /app

RUN groupadd -r app && \
    useradd -r -g app -u 2000 -s /sbin/nologin -d /nonexistent app && \
    mkdir -p /app && chown -R app:app /app

COPY --from=builder --chown=2000:2000 /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","exec java -jar /app/app.jar"]
