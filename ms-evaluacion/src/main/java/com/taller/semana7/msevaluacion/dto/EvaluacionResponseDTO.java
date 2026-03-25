package com.taller.semana7.msevaluacion.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class EvaluacionResponseDTO {

    private String estado;           // APROBADO | DESCARTADO | EN_REVISION_MANUAL
    private BigDecimal montoAprobado;
    private BigDecimal deducibleAplicado;
    private String justificacion;
    private List<String> banderas;
}
