FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY settings.gradle build.gradle ./
COPY eureka-server eureka-server
RUN gradle :eureka-server:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/eureka-server/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
