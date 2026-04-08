package com.taller.semana6.taller_semana6.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reclamo_banderas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamoBandera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamo_id", nullable = false)
    private Reclamo reclamo;

    @Column(nullable = false, length = 500)
    private String descripcionBandera;

    @Column(nullable = false)
    private String reglaOrigen;
}
