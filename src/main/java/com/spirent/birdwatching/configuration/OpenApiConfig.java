package com.spirent.birdwatching.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI birdWatchingApi() {
        return new OpenAPI()
                .info(new Info().title("Bird Watching API")
                        .description("API for managing bird information and sightings")
                        .version("v1.0")
                        .contact(new Contact().name("API Support").email("support@birdwatchingapi.spirent.com"))
                        .license(new License().name("Spirent").url("spirent.birdwatching.com/license")))
                .externalDocs(new ExternalDocumentation()
                        .description("Bird Watching API Documentation")
                        .url("<https://birdwatchingapi.com/docs>"));
    }
}

