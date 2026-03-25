package com.taller.semana7.msapigateway.filter;

import com.taller.semana7.msapigateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Filtro WebFlux global que actúa como guardián JWT del API Gateway.
 *
 * Reglas:
 *  - /api/v1/auth/**  → público (login y registro no requieren token)
 *  - OPTIONS          → público (preflight CORS)
 *  - Todo lo demás    → requiere "Authorization: Bearer {token}" válido
 *
 * Si el token es válido, inyecta X-User-Name, X-User-Role y X-Asegurado-Id
 * en la petición antes de reenviarla al microservicio destino.
 */
@Component
@Order(-100)
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path       = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        // Preflight CORS y rutas públicas → pasan directamente
        if (HttpMethod.OPTIONS.equals(method) || PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("JWT ausente en [{}]", path);
            return writeUnauthorized(exchange, "Token JWT no proporcionado");
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isTokenValid(token)) {
                log.warn("JWT inválido/expirado en [{}]", path);
                return writeUnauthorized(exchange, "Token JWT inválido o expirado");
            }

            String username     = jwtService.extractUsername(token);
            String rol          = jwtService.extractRol(token);
            Long   aseguradoId  = jwtService.extractAseguradoId(token);

            log.debug("JWT válido: user={}, rol={}, aseguradoId={}, path={}", username, rol, aseguradoId, path);

            ServerHttpRequest.Builder builder = exchange.getRequest().mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Role", rol);

            if (aseguradoId != null) {
                builder.header("X-Asegurado-Id", String.valueOf(aseguradoId));
            }

            return chain.filter(exchange.mutate().request(builder.build()).build());

        } catch (Exception e) {
            log.error("Error procesando JWT en [{}]: {}", path, e.getMessage());
            return writeUnauthorized(exchange, "Token JWT inválido");
        }
    }

    private Mono<Void> writeUnauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"error\":\"" + message + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
