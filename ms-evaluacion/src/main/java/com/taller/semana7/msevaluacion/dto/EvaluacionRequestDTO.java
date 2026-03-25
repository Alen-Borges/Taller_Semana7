package com.taller.semana7.msevaluacion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EvaluacionRequestDTO {

    private Long reclamoId;

    @NotNull(message = "El monto estimado es obligatorio")
    @Positive(message = "El monto estimado debe ser positivo")
    private BigDecimal montoEstimado;

    @NotNull(message = "El valor asegurado es obligatorio")
    @Positive(message = "El valor asegurado debe ser positivo")
    private BigDecimal valorAsegurado;

    private String numeroPoliza;
}
