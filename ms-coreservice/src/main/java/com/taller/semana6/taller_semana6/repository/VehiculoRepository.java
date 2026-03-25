package com.taller.semana6.taller_semana6.repository;

import com.taller.semana6.taller_semana6.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    boolean existsByPlaca(String placa);
}
