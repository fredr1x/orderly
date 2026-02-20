FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY settings.gradle build.gradle ./
COPY common-lib common-lib
COPY user-service user-service

RUN gradle :user-service:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/user-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
