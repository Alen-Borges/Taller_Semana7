package com.taller.semana6.taller_semana6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionRequestDTO {
    private Long reclamoId;
    private BigDecimal montoEstimado;
    private BigDecimal valorAsegurado;
    private String numeroPoliza;
}
