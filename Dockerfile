# Use an official slim OpenJDK image for Java 11
FROM openjdk:11-jdk-slim

# Install Maven in the Docker image
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy Maven build file and application source
COPY pom.xml .
COPY src /app/src

# Build the application
RUN mvn clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-Xms256m", "-Xmx512m", "-jar", "target/bird-watching-api-1.0.0.jar"]
