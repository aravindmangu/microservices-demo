FROM openjdk:11
WORKDIR /opt/discovery_server
COPY build/libs/discovery-server*.jar ./discovery-server.jar
ENTRYPOINT ["java","-jar","./discovery-server.jar"]