#base image
FROM openjdk:17-slim

#working dir in container
WORKDIR /app

# app's jar file
COPY target/bookstore-0.0.1-SNAPSHOT.jar /app/bookstore.jar

#make 8080 available
EXPOSE 8080

# run jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/bookstore.jar"]