package com.taller.semana6.taller_semana6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionResponseDTO {
    private String estado;
    private BigDecimal montoAprobado;
    private BigDecimal deducibleAplicado;
    private String justificacion;
    private List<String> banderas;
}
