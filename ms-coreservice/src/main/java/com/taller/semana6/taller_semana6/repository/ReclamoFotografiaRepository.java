package com.taller.semana6.taller_semana6.repository;

import com.taller.semana6.taller_semana6.entity.ReclamoFotografia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamoFotografiaRepository extends JpaRepository<ReclamoFotografia, Long> {
    List<ReclamoFotografia> findByReclamoId(Long reclamoId);
}
