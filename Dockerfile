FROM openjdk:17-jdk
WORKDIR /app
COPY target/flightsearch-0.0.1-SNAPSHOT.jar app.jar
COPY target/google-services.json google-services.json
CMD ["java", "-jar", "app.jar"]