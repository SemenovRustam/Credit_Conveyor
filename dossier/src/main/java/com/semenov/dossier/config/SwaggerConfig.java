package com.semenov.dossier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket getApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.semenov.dossier.controller"))
                .paths(regex("/api.*"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Dossier service",
                "credit conveyor API.",
                "0.1",
                "Terms of service",
                new Contact(
                        "Rustam Semenov",
                        "https://vk.com/rustamkaa17",
                        "zeevvsss@yandex.ru"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }
}
