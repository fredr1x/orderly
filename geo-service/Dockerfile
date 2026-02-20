FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY settings.gradle build.gradle ./
COPY common-lib common-lib
COPY geo-service geo-service

RUN gradle :geo-service:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/geo-service/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
