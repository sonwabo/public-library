FROM openjdk:17-alpine

WORKDIR /app

COPY target/online-public-library.jar .

EXPOSE 8080

LABEL image.name="online-public-library"
LABEL image.description="Spring Boot Application"
LABEL image.version="1.0"

CMD ["java", "-jar", "online-public-library.jar"]