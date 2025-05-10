
# Use an official JDK image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the full project and build it
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the app
#FROM eclipse-temurin:17-jdk-alpine
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/AntiSolo-0.0.1-SNAPSHOT.jar .

# Expose the port your app runs on (default is 8080)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/app/AntiSolo-0.0.1-SNAPSHOT.jar"]

## Use an official JDK image to build the app
#FROM maven:3.9.6-eclipse-temurin-17 AS build
#
## Set the working directory
#WORKDIR /app
#
## Copy pom.xml and download dependencies (layer caching)
#COPY pom.xml .
#RUN mvn dependency:go-offline -B
#
## Copy the full project and build it
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Use a lightweight JDK image to run the app
#FROM openjdk:17-jdk-slim
#
## 🔥 Fix for MongoDB Atlas SSL error
#RUN apt-get update && apt-get install -y ca-certificates && apt-get clean
#
## Set the working directory
#WORKDIR /app
#
## Copy the jar from the build stage
#COPY --from=build /app/target/AntiSolo-0.0.1-SNAPSHOT.jar .
#
## Expose the port your app runs on (default is 8080)
#EXPOSE 8080
#
## Run the app
#ENTRYPOINT ["java", "-jar", "/app/AntiSolo-0.0.1-SNAPSHOT.jar"]

