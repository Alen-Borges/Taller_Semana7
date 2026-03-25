package com.taller.semana7.msapigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Proxy inverso reactivo del API Gateway.
 *
 * Redirige las peticiones al microservicio correspondiente según el prefijo de path:
 *   /api/v1/auth/**                               → auth-service       :8081
 *   /api/v1/asegurados|vehiculos|polizas|reclamos → core-service       :8082
 *   /api/v1/evaluar/**                            → evaluacion-service :8083
 *
 * Preserva: método HTTP, headers (incluyendo X-User-Name, X-User-Role y
 * X-Asegurado-Id inyectados por el JwtAuthenticationFilter), query params y body.
 */
@RestController
@Slf4j
public class ProxyController {

    private final WebClient authWebClient;
    private final WebClient coreWebClient;
    private final WebClient evaluacionWebClient;

    public ProxyController(
            @Qualifier("authWebClient") WebClient authWebClient,
            @Qualifier("coreWebClient") WebClient coreWebClient,
            @Qualifier("evaluacionWebClient") WebClient evaluacionWebClient) {
        this.authWebClient = authWebClient;
        this.coreWebClient = coreWebClient;
        this.evaluacionWebClient = evaluacionWebClient;
    }

    // ─── Auth-service ─────────────────────────────────────────────────────────
    // El auth-service expone /auth/**; el gateway recibe /api/v1/auth/**

    @RequestMapping("/api/v1/auth/**")
    public Mono<ResponseEntity<byte[]>> proxyAuth(ServerHttpRequest request) {
        String targetPath = request.getURI().getPath().replaceFirst("/api/v1/auth", "/auth");
        String query = request.getURI().getRawQuery();
        String uri = query != null ? targetPath + "?" + query : targetPath;
        log.debug("→ auth-service: {} {}", request.getMethod(), uri);
        return proxy(authWebClient, request, uri);
    }

    // ─── Core-service ─────────────────────────────────────────────────────────

    @RequestMapping({
            "/api/v1/asegurados/**",
            "/api/v1/vehiculos/**",
            "/api/v1/polizas/**",
            "/api/v1/reclamos/**"
    })
    public Mono<ResponseEntity<byte[]>> proxyCore(ServerHttpRequest request) {
        String targetPath = request.getURI().getPath();
        String query = request.getURI().getRawQuery();
        String uri = query != null ? targetPath + "?" + query : targetPath;
        log.debug("→ core-service: {} {}", request.getMethod(), uri);
        return proxy(coreWebClient, request, uri);
    }

    // ─── Evaluacion-service ───────────────────────────────────────────────────

    @RequestMapping("/api/v1/evaluar/**")
    public Mono<ResponseEntity<byte[]>> proxyEvaluacion(ServerHttpRequest request) {
        String targetPath = request.getURI().getPath();
        String query = request.getURI().getRawQuery();
        String uri = query != null ? targetPath + "?" + query : targetPath;
        log.debug("→ evaluacion-service: {} {}", request.getMethod(), uri);
        return proxy(evaluacionWebClient, request, uri);
    }

    // ─── Proxy genérico ───────────────────────────────────────────────────────

    private Mono<ResponseEntity<byte[]>> proxy(WebClient client, ServerHttpRequest request, String uri) {
        return client.method(request.getMethod())
                .uri(uri)
                .headers(headers -> copyHeaders(request.getHeaders(), headers))
                .body(request.getBody(), byte[].class)
                .retrieve()
                .toEntity(byte[].class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity.status(ex.getStatusCode())
                                .headers(ex.getHeaders())
                                .body(ex.getResponseBodyAsByteArray()))
                )
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error en proxy: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(502)
                            .body(("{\"error\":\"Servicio no disponible\"}").getBytes()));
                });
    }

    private void copyHeaders(HttpHeaders source, HttpHeaders target) {
        // Copiar todos los headers excepto los que Netty gestiona automáticamente
        source.forEach((name, values) -> {
            String lower = name.toLowerCase();
            if (!lower.equals("host") && !lower.equals("connection") &&
                !lower.equals("transfer-encoding") && !lower.equals("keep-alive")) {
                target.put(name, values);
            }
        });
    }
}
