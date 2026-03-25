package com.taller.semana7.msevaluacion.repository;

import com.taller.semana7.msevaluacion.entity.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByReclamoId(Long reclamoId);
}
