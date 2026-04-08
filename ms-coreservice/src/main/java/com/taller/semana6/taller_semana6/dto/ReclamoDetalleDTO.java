package com.taller.semana6.taller_semana6.dto;

import com.taller.semana6.taller_semana6.entity.EstadoReclamo;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Detalle completo del reclamo para el gestor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamoDetalleDTO {

    private Long id;
    private String numeroSeguimiento;
    private EstadoReclamo estado;
    private String motivoDecision;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;

    // Datos del incidente
    private LocalDate fechaIncidente;
    private String descripcion;
    private String ubicacion;
    private BigDecimal montoEstimado;
    private BigDecimal deducibleCalculado;
    private BigDecimal montoAprobado;

    // Póliza completa
    private Long polizaId;
    private String polizaNumero;
    private String polizaEstado;
    private LocalDate vigenciaInicio;
    private LocalDate vigenciaFin;
    private BigDecimal valorAsegurado;

    // Asegurado completo
    private Long aseguradoId;
    private String aseguradoNombre;
    private String aseguradoApellido;
    private String aseguradoNumeroIdentificacion;
    private String aseguradoCorreo;
    private String aseguradoTelefono;

    // Vehículo
    private Long vehiculoId;
    private String vehiculoMarca;
    private String vehiculoModelo;
    private Integer vehiculoAnio;
    private String vehiculoPlaca;

    // Adjuntos y banderas
    private List<String> urlsFotografias;
    private List<String> banderas;
}
