# syntax=docker/dockerfile:1.4
ARG JDK_VERSION=21

####################
# Build stage
####################
# usar uma tag válida com o JDK 21
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

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","exec java -jar /app/app.jar"]
