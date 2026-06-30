package com.chipping.envios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Envios API - Chipping")
                .description("Gestión de envíos y seguimiento de despachos")
                .version("v1.0"));
    }
}