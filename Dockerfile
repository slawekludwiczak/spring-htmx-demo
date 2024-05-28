FROM eclipse-temurin:21-jdk-jammy AS maven-build
COPY .mvn /build/.mvn
COPY mvnw pom.xml /build/
WORKDIR /build
RUN --mount=type=cache,target=/root/.m2/ ./mvnw dependency:go-offline
COPY src /build/src
RUN --mount=type=cache,target=/root/.m2/ ./mvnw clean package

FROM eclipse-temurin:21-alpine AS application
COPY --from=maven-build /build/target/movieclub-*.jar /opt/app.jar
WORKDIR /opt
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
