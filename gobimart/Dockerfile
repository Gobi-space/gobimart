# Multi-stage Docker build for Spring Boot application (Render deployment)

# Stage 1: Build the JAR with Maven & Eclipse Temurin JDK 17
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /workspace

# Copy pom.xml and download dependencies first (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and package executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal, secure runtime using Eclipse Temurin JRE 17 Alpine (~140MB)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user for container security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create directory for file-based H2 database persistence
RUN mkdir -p /app/data && chown -R appuser:appgroup /app

# Copy built JAR from builder stage
COPY --from=builder /workspace/target/gobimart-1.0.0.jar app.jar

USER appuser

# Render dynamically sets PORT env var (defaults to 8080 locally)
ENV PORT=8080

# JVM memory flags tuned for Render free tier (512MB RAM):
# - MaxRAMPercentage=75 restricts heap to ~384MB, reserving ~128MB for Metaspace/OS to prevent OOM exit 137
# - Xss512k lowers thread stack footprint
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Xss512k -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
