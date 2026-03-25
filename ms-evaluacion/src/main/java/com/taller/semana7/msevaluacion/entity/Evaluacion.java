package com.taller.semana7.msevaluacion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluaciones", schema = "eval_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reclamoId;

    private String numeroPoliza;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montoEstimado;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAsegurado;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal deducibleAplicado;

    @Column(precision = 15, scale = 2)
    private BigDecimal montoAprobado;

    @Column(nullable = false)
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String justificacion;

    @Column(nullable = false)
    private LocalDateTime fechaEvaluacion;
}
