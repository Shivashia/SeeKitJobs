# Stage 1: Build the application using Maven
FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app

# Copy the Maven project files and dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a slim JDK image
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/jobportal.jar jobportal.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint to run the jar file
ENTRYPOINT ["java", "-jar", "/jobportal.jar"]
