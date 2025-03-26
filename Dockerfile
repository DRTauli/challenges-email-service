# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the src directory into the container
COPY pom.xml .
COPY src ./src

# Build the application; skip tests for faster build
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal runtime environment
FROM openjdk:17-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/email-service-0.0.1.jar app.jar

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port the application runs on
EXPOSE 8080