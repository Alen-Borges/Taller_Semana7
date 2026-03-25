package com.taller.semana6.taller_semana6.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reclamo_fotografias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamoFotografia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamo_id", nullable = false)
    private Reclamo reclamo;

    @Column(nullable = false)
    private String urlFotografia;

    @Column(nullable = false)
    private String nombreArchivo;
}
