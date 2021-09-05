# microservices-demo

Non-Functional Requirements
-	Need to use vault or any other service to store sensitive information
-	Need to use config-server for dynamically updating configurations from github
-	Authorization provided at API gateway level, need to provide fine grain method level protection
-	Need to use circuit breaker using something like Resilience4j
-	Need to use centralized logging using services like ELK stack
-	Need to use proper distributed caching mechanism
