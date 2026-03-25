package com.taller.semana6.taller_semana6.controller;

import com.taller.semana6.taller_semana6.dto.AseguradoDTO;
import com.taller.semana6.taller_semana6.service.AseguradoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asegurados")
@RequiredArgsConstructor
public class AseguradoController {

    private final AseguradoService aseguradoService;

    @PostMapping
    public ResponseEntity<AseguradoDTO> registrarAsegurado(@Valid @RequestBody AseguradoDTO aseguradoDTO) {
        return new ResponseEntity<>(aseguradoService.registrarAsegurado(aseguradoDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AseguradoDTO>> consultarAsegurados() {
        return ResponseEntity.ok(aseguradoService.consultarAsegurados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AseguradoDTO> consultarAseguradoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(aseguradoService.consultarAseguradoPorId(id));
    }
}
