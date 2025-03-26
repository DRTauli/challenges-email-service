# Use a base image with JDK
FROM openjdk:11-jre

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/email-service-0.0.1.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port your app runs on
EXPOSE 8080