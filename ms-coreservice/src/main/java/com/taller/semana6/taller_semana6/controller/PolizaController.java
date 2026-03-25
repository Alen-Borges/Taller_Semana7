package com.taller.semana6.taller_semana6.controller;

import com.taller.semana6.taller_semana6.dto.PolizaRequestDTO;
import com.taller.semana6.taller_semana6.dto.PolizaResponseDTO;
import com.taller.semana6.taller_semana6.service.PolizaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
public class PolizaController {

    private final PolizaService polizaService;

    @PostMapping
    public ResponseEntity<PolizaResponseDTO> registrarPoliza(@Valid @RequestBody PolizaRequestDTO polizaRequestDTO) {
        return new ResponseEntity<>(polizaService.registrarPoliza(polizaRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PolizaResponseDTO>> consultarPolizas() {
        return ResponseEntity.ok(polizaService.consultarPolizas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolizaResponseDTO> consultarPolizaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.consultarPolizaPorId(id));
    }
}
