FROM openjdk:11
WORKDIR /opt/client_service
COPY build/libs/client-service*.jar ./client-service.jar
ENTRYPOINT ["java","-jar","./client-service.jar"]