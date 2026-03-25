package com.taller.semana7.msauthservice.controller;

import com.taller.semana7.msauthservice.dto.LoginRequestDTO;
import com.taller.semana7.msauthservice.dto.LoginResponseDTO;
import com.taller.semana7.msauthservice.dto.RegisterRequestDTO;
import com.taller.semana7.msauthservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * POST /auth/login    → autentica y devuelve JWT
 * POST /auth/register → crea nuevo usuario
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return new ResponseEntity<>(
                Map.of("message", "Usuario '" + request.getUsername() + "' registrado correctamente"),
                HttpStatus.CREATED
        );
    }
}
