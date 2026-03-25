package com.taller.semana6.taller_semana6.repository;

import com.taller.semana6.taller_semana6.entity.Poliza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolizaRepository extends JpaRepository<Poliza, Long> {
    Optional<Poliza> findByNumero(String numero);
    boolean existsByNumero(String numero);
}
