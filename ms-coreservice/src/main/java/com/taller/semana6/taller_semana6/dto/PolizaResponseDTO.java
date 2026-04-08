package com.taller.semana6.taller_semana6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolizaResponseDTO {
    private Long id;
    private String numero;
    private BigDecimal valorAsegurado;
    private LocalDate vigenciaInicio;
    private LocalDate vigenciaFin;
    private String estado;

    // Asegurado Summary
    private Long aseguradoId;
    private String aseguradoNombre;

    // Vehiculo Summary
    private Long vehiculoId;
    private String vehiculoMarcaModeloPlaca;
}
