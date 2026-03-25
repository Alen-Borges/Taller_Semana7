package com.taller.semana7.msapigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("authWebClient")
    public WebClient authWebClient(@Value("${services.auth-url}") String authUrl) {
        return WebClient.builder().baseUrl(authUrl).build();
    }

    @Bean("coreWebClient")
    public WebClient coreWebClient(@Value("${services.core-url}") String coreUrl) {
        return WebClient.builder().baseUrl(coreUrl).build();
    }

    @Bean("evaluacionWebClient")
    public WebClient evaluacionWebClient(@Value("${services.evaluacion-url}") String evaluacionUrl) {
        return WebClient.builder().baseUrl(evaluacionUrl).build();
    }
}
