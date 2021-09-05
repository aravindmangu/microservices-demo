FROM openjdk:11

COPY wait-for-it.sh /usr/wait-for-it.sh
RUN chmod +x /usr/wait-for-it.sh

WORKDIR /opt/api_gateway
COPY build/libs/api-gateway*.jar ./api-gateway.jar
ENTRYPOINT ["java","-jar","./api-gateway.jar"]