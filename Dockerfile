FROM openjdk:17-jdk-slim
EXPOSE 8282
WORKDIR /app
COPY target/rate-and-review-service.jar /app/rate-and-review-service.jar
ENTRYPOINT ["java","-jar","/app/rate-and-review-service.jar"]