package au.com.demo.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import au.com.demo.apigateway.filter.JwtAuthenticationFilter;

@Configuration
public class SpringConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes().route("auth", r -> r.path("/auth/**").filters(f -> f.filter(filter)).uri("lb://api-gateway-auth"))
                //.route("swagger-ui", r -> r.path("/swagger-ui/**").filters(f -> f.filter(filter)).uri("lb://client-service"))
                .route("client", r -> r.path("/client/**").filters(f -> f.filter(filter)).uri("lb://client-service")).build();
    }

}
