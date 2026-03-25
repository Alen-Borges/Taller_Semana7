package com.taller.semana7.msauthservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios", schema = "auth_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /**
     * ID del asegurado vinculado (sólo para rol ASEGURADO).
     * Para rol GESTOR queda null.
     * Se incrusta como claim "aseguradoId" en el JWT para que el gateway
     * lo propague como header X-Asegurado-Id al core-service.
     */
    @Column(name = "asegurado_id")
    private Long aseguradoId;
}
