package com.taller.semana6.taller_semana6.controller;

import com.taller.semana6.taller_semana6.dto.VehiculoRequestDTO;
import com.taller.semana6.taller_semana6.dto.VehiculoResponseDTO;
import com.taller.semana6.taller_semana6.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<VehiculoResponseDTO> registrarVehiculo(@Valid @RequestBody VehiculoRequestDTO vehiculoRequestDTO) {
        return new ResponseEntity<>(vehiculoService.registrarVehiculo(vehiculoRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VehiculoResponseDTO>> consultarVehiculos() {
        return ResponseEntity.ok(vehiculoService.consultarVehiculos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> consultarVehiculoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.consultarVehiculoPorId(id));
    }
}
