package com.taller.semana6.taller_semana6.client;

import com.taller.semana6.taller_semana6.dto.AuthRegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClientImpl implements AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url:http://localhost:8081}")
    private String authServiceUrl;

    @Override
    public void registrarUsuario(AuthRegisterRequestDTO request) {
        String url = authServiceUrl + "/auth/register";
        try {
            log.info("[core-service] POST {} — username={}", url, request.getUsername());
            restTemplate.postForObject(url, request, Object.class);
            log.info("[core-service] auth-service: usuario '{}' creado correctamente", request.getUsername());
        } catch (RestClientException e) {
            log.error("[core-service] auth-service no disponible al crear usuario '{}': {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("No se pudo crear la cuenta de acceso del asegurado: " + e.getMessage());
        }
    }
}
