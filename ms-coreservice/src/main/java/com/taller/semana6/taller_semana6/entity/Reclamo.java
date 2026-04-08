package com.taller.semana6.taller_semana6.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reclamos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroSeguimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliza_id", nullable = false)
    private Poliza poliza;

    @Column(nullable = false)
    private LocalDate fechaIncidente;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montoEstimado;

    @Column(nullable = false)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReclamo estado;

    // Campos calculados por el motor de reglas
    @Column(precision = 12, scale = 2)
    private BigDecimal deducibleCalculado;

    @Column(precision = 12, scale = 2)
    private BigDecimal montoAprobado;

    @Column(length = 2000)
    private String motivoDecision;

    private LocalDateTime fechaResolucion;

    // Para futura integración con autenticación
    private Long gestorResponsableId;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "reclamo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReclamoFotografia> fotografias = new ArrayList<>();

    @OneToMany(mappedBy = "reclamo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReclamoBandera> banderas = new ArrayList<>();
}

