package com.taller.semana6.taller_semana6.dto;

import com.taller.semana6.taller_semana6.entity.EstadoReclamo;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resumen de reclamo para el panel del gestor (listado de escalados).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamoEscaladoDTO {

    private String numeroSeguimiento;
    private EstadoReclamo estado;
    private LocalDateTime fechaCreacion;
    private BigDecimal montoEstimado;
    private BigDecimal deducibleCalculado;

    // Asegurado
    private Long aseguradoId;
    private String aseguradoNombreCompleto;
    private String aseguradoNumeroIdentificacion;

    // Póliza
    private Long polizaId;
    private String polizaNumero;

    // Banderas rojas detectadas
    private List<String> banderas;
}
