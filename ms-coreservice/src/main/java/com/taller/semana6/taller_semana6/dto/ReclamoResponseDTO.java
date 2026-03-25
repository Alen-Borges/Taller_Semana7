package com.taller.semana6.taller_semana6.dto;

import com.taller.semana6.taller_semana6.entity.EstadoReclamo;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamoResponseDTO {

    private Long id;
    private String numeroSeguimiento;
    private EstadoReclamo estado;

    // Datos del incidente
    private LocalDate fechaIncidente;
    private String descripcion;
    private String ubicacion;
    private BigDecimal montoEstimado;

    // Resultados del motor de reglas
    private BigDecimal deducibleCalculado;
    private BigDecimal montoAprobado;
    private String motivoDecision;
    private LocalDateTime fechaResolucion;

    // Resumen de póliza
    private Long polizaId;
    private String polizaNumero;

    // Resumen de asegurado
    private Long aseguradoId;
    private String aseguradoNombreCompleto;

    // Resumen de vehículo
    private String vehiculoMarcaModeloPlaca;

    // Auditoría
    private LocalDateTime fechaCreacion;

    // Adjuntos y banderas
    private List<String> urlsFotografias;
    private List<String> banderas;
}
