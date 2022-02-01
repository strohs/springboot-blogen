package com.blogen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 (Swagger) config for the Blogen REST API.
 * using the SpringDoc library,
 *
 */
@Configuration
public class SwaggerConfig {

    // We only have one docket for the Blogen API, so we can do configuration
    // in application.properties instead of the bean below:
    // springdoc.packagesToScan=com.blogen.api
    // springdoc.pathsToMatch=/api/**

//    @Bean
//    public GroupedOpenApi api() {
//        return GroupedOpenApi.builder()
//                .group("blogen-public")
//                .pathsToMatch("/api/**")
//                .build();
//    }

    @Bean
    public OpenAPI blogenOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Blogen API")
                        .description("Blogen sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://github.com/strohs")));
    }


}
