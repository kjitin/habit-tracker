# syntax=docker/dockerfile:1.7

# ---- Stage 1: Build ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

# Copy build configuration first so dependency download caches.
COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY platform-libs platform-libs
COPY services/habit-service/build.gradle.kts services/habit-service/build.gradle.kts

# Pre-download dependencies. This layer caches as long as build files don't change.
RUN ./gradlew :services:habit-service:dependencies --no-daemon || true

# Source code last, so changing it doesn't bust the dependency cache.
COPY services/habit-service/src services/habit-service/src

RUN ./gradlew :services:habit-service:bootJar --no-daemon

# Extract layered JAR for better Docker layer caching at runtime.
RUN mkdir -p /workspace/extracted && \
    cd /workspace/extracted && \
    java -Djarmode=tools -jar /workspace/services/habit-service/build/libs/*.jar extract --layers --launcher

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:21-jre-alpine AS runtime

# Non-root user. Always run containers as non-root.
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

# Copy each layer separately. Order matters: least-volatile first.
COPY --from=build --chown=app:app /workspace/extracted/dependencies/ ./
COPY --from=build --chown=app:app /workspace/extracted/spring-boot-loader/ ./
COPY --from=build --chown=app:app /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build --chown=app:app /workspace/extracted/application/ ./

USER app

EXPOSE 8080 8081

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "org.springframework.boot.loader.launch.JarLauncher"]
