package com.taller.semana6.taller_semana6.client;

import com.taller.semana6.taller_semana6.dto.EvaluacionRequestDTO;
import com.taller.semana6.taller_semana6.dto.EvaluacionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvaluacionServiceClientImpl implements EvaluacionServiceClient {

    private final RestTemplate restTemplate;

    @Value("${evaluacion.service.url:http://localhost:8083}")
    private String evaluacionServiceUrl;

    @Override
    public EvaluacionResponseDTO evaluar(EvaluacionRequestDTO request) {
        String url = evaluacionServiceUrl + "/api/v1/evaluar";
        try {
            log.info("[core-service] POST {} — reclamoId={}", url, request.getReclamoId());
            EvaluacionResponseDTO response = restTemplate.postForObject(url, request, EvaluacionResponseDTO.class);
            log.info("[core-service] evaluacion-service respondió: estado={}", response != null ? response.getEstado() : "null");
            return response;
        } catch (RestClientException e) {
            log.warn("[core-service] evaluacion-service no disponible ({}). Reclamo queda en estado REGISTRADO.", e.getMessage());
            return null;
        }
    }
}
