package com.taller.semana6.taller_semana6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asegurados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asegurado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String numeroIdentificacion;

    private String direccion;

    private String telefono;

    @Column(nullable = false, unique = true)
    private String correoElectronico;

    @OneToMany(mappedBy = "asegurado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Vehiculo> vehiculos;
}
