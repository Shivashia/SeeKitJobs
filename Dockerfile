#FROM maven:3.8.5-openjdk-17 AS build
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17-jdk-slim
#COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","demo.jar"]


# Stage 1: Build the application
FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app

# Only copy necessary files to minimize build cache invalidation
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source files and package the application
COPY src ./src
RUN mvn clean package

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/jobportal-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Use CMD for the default run command (can be overridden)
CMD ["java", "-jar", "/app.jar"]
