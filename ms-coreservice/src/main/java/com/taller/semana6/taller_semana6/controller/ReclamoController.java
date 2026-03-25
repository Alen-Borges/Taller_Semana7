package com.taller.semana6.taller_semana6.controller;

import com.taller.semana6.taller_semana6.dto.*;
import com.taller.semana6.taller_semana6.service.ReclamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Endpoints del dominio Reclamos:
 *
 * POST  /api/v1/reclamos                    → HU-007: Registrar reclamo (multipart, mín. 1 foto)
 * GET   /api/v1/reclamos/escalados          → HU-011: Panel gestor — reclamos EN_REVISION_MANUAL
 * GET   /api/v1/reclamos/{num}/detalle      → HU-011: Detalle completo para gestor
 * PUT   /api/v1/reclamos/{num}/resolucion   → HU-012: Resolución manual del gestor
 * GET   /api/v1/reclamos/{num}/estado       → HU-013: Estado del reclamo para asegurado
 */
@RestController
@RequestMapping("/api/v1/reclamos")
@RequiredArgsConstructor
public class ReclamoController {

    private final ReclamoService reclamoService;

    // ─── HU-007: Registrar reclamo ────────────────────────────────────────
    // Flujo sincrónico: core-service llama evaluacion-service y retorna estado final.
    // Spec §8.1: mínimo 1 fotografía obligatoria (validado en servicio).

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReclamoResponseDTO> registrarReclamo(
            @RequestParam("polizaId")       Long polizaId,
            @RequestParam("fechaIncidente") LocalDate fechaIncidente,
            @RequestParam("descripcion")    String descripcion,
            @RequestParam("montoEstimado")  BigDecimal montoEstimado,
            @RequestParam("ubicacion")      String ubicacion,
            @RequestParam(value = "fotografias", required = false) List<MultipartFile> fotografias) {

        ReclamoRequestDTO dto = ReclamoRequestDTO.builder()
                .polizaId(polizaId).fechaIncidente(fechaIncidente)
                .descripcion(descripcion).montoEstimado(montoEstimado).ubicacion(ubicacion)
                .build();

        return new ResponseEntity<>(reclamoService.registrarReclamo(dto, fotografias), HttpStatus.CREATED);
    }

    // ─── HU-011: Panel gestor ─────────────────────────────────────────────

    @GetMapping("/escalados")
    public ResponseEntity<List<ReclamoEscaladoDTO>> consultarEscalados() {
        return ResponseEntity.ok(reclamoService.consultarEscalados());
    }

    @GetMapping("/{numeroSeguimiento}/detalle")
    public ResponseEntity<ReclamoDetalleDTO> consultarDetalle(@PathVariable String numeroSeguimiento) {
        return ResponseEntity.ok(reclamoService.consultarDetalle(numeroSeguimiento));
    }

    // ─── HU-012: Resolución manual ────────────────────────────────────────

    @PutMapping("/{numeroSeguimiento}/resolucion")
    public ResponseEntity<ReclamoResponseDTO> resolverManualmente(
            @PathVariable String numeroSeguimiento,
            @Valid @RequestBody ResolucionManualRequestDTO dto) {
        return ResponseEntity.ok(reclamoService.resolverManualmente(numeroSeguimiento, dto));
    }

    // ─── HU-013: Estado asegurado ─────────────────────────────────────────

    @GetMapping("/{numeroSeguimiento}/estado")
    public ResponseEntity<EstadoReclamoDTO> consultarEstado(@PathVariable String numeroSeguimiento) {
        return ResponseEntity.ok(reclamoService.consultarEstado(numeroSeguimiento));
    }
}
