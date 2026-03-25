package com.taller.semana6.taller_semana6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "polizas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asegurado_id", nullable = false)
    private Asegurado asegurado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(nullable = false)
    private BigDecimal valorAsegurado;

    @Column(nullable = false)
    private LocalDate vigenciaInicio;

    @Column(nullable = false)
    private LocalDate vigenciaFin;

    @Column(nullable = false)
    private String estado; // ACTIVA, INACTIVA, etc.
}
