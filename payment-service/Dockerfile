FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY settings.gradle build.gradle ./
COPY common-lib common-lib
COPY payment-service payment-service

RUN gradle :payment-service:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/payment-service/build/libs/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
