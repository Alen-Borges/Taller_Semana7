package com.taller.semana6.taller_semana6.repository;

import com.taller.semana6.taller_semana6.entity.Asegurado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AseguradoRepository extends JpaRepository<Asegurado, Long> {
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByCorreoElectronico(String correoElectronico);
}
