FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} Resource-Finder-Backend.jar

ENTRYPOINT ["java", "-jar", "/File-Server-Backend-0.0.1.jar"]