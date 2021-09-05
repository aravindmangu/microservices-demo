package au.com.demo.clientservice.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfo("Client Management API",
                        "Client Management API",
                        "1.0", null, new Contact("Demo", "", ""),
                        "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
                        new ArrayList<>()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("au.com.demo.clientservice"))
                .paths(PathSelectors.regex("/client/.*"))
                .build();
    }
}
