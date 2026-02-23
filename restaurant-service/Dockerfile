FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY settings.gradle build.gradle ./
COPY common-lib common-lib
COPY restaurant-service restaurant-service

RUN gradle :restaurant-service:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/restaurant-service/build/libs/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
