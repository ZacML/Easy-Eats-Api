package com.easy.eats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eatsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Easy Eats API")
                        .description("API de gestão para restaurantes: cardápio, produtos, pedidos, vendas, estoque e financeiro.")
                        .version("v0.0.1")
                        .contact(new Contact().name("Easy Eats")));
    }
}
