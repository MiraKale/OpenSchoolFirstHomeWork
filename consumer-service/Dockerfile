FROM openjdk:17-jdk-alpine
COPY ./target/consumer-service-0.0.1-SNAPSHOT.jar consumer-service.jar
EXPOSE 8081
CMD ["java","-jar","consumer-service.jar"]