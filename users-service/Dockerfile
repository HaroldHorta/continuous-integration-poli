FROM openjdk:8-jdk-alpine
ADD target/users-service-0.0.1-SNAPSHOT.jar user-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/user-service.jar"]