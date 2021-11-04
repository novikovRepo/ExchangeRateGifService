FROM openjdk:11-jdk-alpine

EXPOSE 8030

COPY exchange-rate-gif-service-0.0.1.jar exchange-rate-gif-service-0.0.1.jar

CMD ["java","-jar","exchange-rate-gif-service-0.0.1.jar"]