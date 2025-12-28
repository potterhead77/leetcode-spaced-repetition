# 1. Build Stage
# Use a newer, more reliable Maven image
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Run Stage
# Use a smaller, production-ready runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Add "-Xmx350m" to limit Java to 350MB of RAM
# 256MB Heap + ~150MB Overhead = ~400MB (Safe for 512MB container)
ENTRYPOINT ["java", "-Xmx256m", "-jar", "app.jar"]