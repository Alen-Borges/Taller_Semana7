package com.taller.semana6.taller_semana6.dto;

import com.taller.semana6.taller_semana6.entity.EstadoReclamo;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Estado simplificado del reclamo para consulta del asegurado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoReclamoDTO {

    private String numeroSeguimiento;
    private EstadoReclamo estado;
    private String mensajeEstado;          // Mensaje amigable según el estado
    private BigDecimal montoEstimado;
    private BigDecimal deducibleCalculado;
    private BigDecimal montoAprobado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;
}
