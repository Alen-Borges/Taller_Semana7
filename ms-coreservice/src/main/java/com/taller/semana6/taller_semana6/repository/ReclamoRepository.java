package com.taller.semana6.taller_semana6.repository;

import com.taller.semana6.taller_semana6.entity.EstadoReclamo;
import com.taller.semana6.taller_semana6.entity.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {

    Optional<Reclamo> findByNumeroSeguimiento(String numeroSeguimiento);

    List<Reclamo> findByEstado(EstadoReclamo estado);

    boolean existsByNumeroSeguimiento(String numeroSeguimiento);

    /**
     * Cuenta los siniestros de un asegurado en los últimos 12 meses,
     * excluyendo los DESCARTADOS (que no son siniestros reales).
     */
    @Query("SELECT COUNT(r) FROM Reclamo r " +
           "WHERE r.poliza.asegurado.id = :aseguradoId " +
           "AND r.fechaIncidente >= :fechaDesde " +
           "AND r.estado NOT IN :estadosExcluidos")
    long contarSiniestrosPorAsegurado(
            @Param("aseguradoId") Long aseguradoId,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("estadosExcluidos") List<EstadoReclamo> estadosExcluidos);

    /**
     * Cuenta registros del año actual para generar número de seguimiento.
     */
    @Query("SELECT COUNT(r) FROM Reclamo r " +
           "WHERE r.numeroSeguimiento LIKE :prefixLike")
    long contarPorAnio(@Param("prefixLike") String prefixLike);
}
