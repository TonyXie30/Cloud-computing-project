# Use Maven to build the project
FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN rm -f ./src/main/resources/application.properties
RUN mvn clean package spring-boot:repackage -DskipTests

# Use OpenJDK to run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8446 8080
CMD ["java", "-jar", "app.jar"]
