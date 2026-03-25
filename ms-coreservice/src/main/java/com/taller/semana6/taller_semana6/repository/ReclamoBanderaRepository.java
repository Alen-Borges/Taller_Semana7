package com.taller.semana6.taller_semana6.repository;

import com.taller.semana6.taller_semana6.entity.ReclamoBandera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamoBanderaRepository extends JpaRepository<ReclamoBandera, Long> {
    List<ReclamoBandera> findByReclamoId(Long reclamoId);
}

