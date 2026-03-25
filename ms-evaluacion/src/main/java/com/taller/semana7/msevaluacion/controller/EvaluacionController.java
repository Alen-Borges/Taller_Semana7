package com.taller.semana7.msevaluacion.controller;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;
import com.taller.semana7.msevaluacion.service.ClaimEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * POST /api/v1/evaluar — HU-009
 * Llamado por core-service de forma síncrona tras registrar un Reclamo.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EvaluacionController {

    private final ClaimEvaluationService claimEvaluationService;

    @PostMapping("/evaluar")
    public ResponseEntity<EvaluacionResponseDTO> evaluar(
            @Valid @RequestBody EvaluacionRequestDTO request) {
        return ResponseEntity.ok(claimEvaluationService.evaluar(request));
    }
}
