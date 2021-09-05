FROM openjdk:11

WORKDIR /opt/api_gateway
COPY build/libs/api-gateway-auth*.jar ./api-gateway-auth.jar
ENTRYPOINT ["java","-jar","./api-gateway-auth.jar"]