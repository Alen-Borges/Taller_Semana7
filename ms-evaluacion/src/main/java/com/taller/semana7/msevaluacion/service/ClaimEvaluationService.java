package com.taller.semana7.msevaluacion.service;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;
import com.taller.semana7.msevaluacion.entity.Evaluacion;
import com.taller.semana7.msevaluacion.repository.EvaluacionRepository;
import com.taller.semana7.msevaluacion.service.rules.EvaluacionRule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Orquestador del motor de reglas (patrón Strategy).
 *
 * Recibe la lista de EvaluacionRule inyectada por Spring (ordenada por @Order).
 * Ejecuta cada regla en orden; la primera que retorne un resultado terminal
 * detiene la cadena. Si ninguna regla aplica, el reclamo se APRUEBA.
 *
 * Principio DIP: depende de la abstracción EvaluacionRule, no de implementaciones concretas.
 * Principio OCP: agregar una nueva regla no requiere modificar este servicio.
 */
@Service
public class ClaimEvaluationService {

    private final List<EvaluacionRule> rules;
    private final EvaluacionRepository evaluacionRepository;

    public ClaimEvaluationService(List<EvaluacionRule> rules, EvaluacionRepository evaluacionRepository) {
        this.rules = rules;
        this.evaluacionRepository = evaluacionRepository;
    }

    public EvaluacionResponseDTO evaluar(EvaluacionRequestDTO request) {
        BigDecimal deducible = calcularDeducible(request.getMontoEstimado(), request.getValorAsegurado());

        // Ejecutar reglas en orden — primera terminal gana
        EvaluacionResponseDTO resultado = null;
        for (EvaluacionRule rule : rules) {
            Optional<EvaluacionResponseDTO> res = rule.evaluate(request, deducible);
            if (res.isPresent()) {
                resultado = res.get();
                break;
            }
        }

        // Ninguna regla disparó → Regla R2: APROBADO
        if (resultado == null) {
            BigDecimal montoAprobado = request.getMontoEstimado().subtract(deducible);
            resultado = EvaluacionResponseDTO.builder()
                    .estado("APROBADO")
                    .montoAprobado(montoAprobado)
                    .deducibleAplicado(deducible)
                    .justificacion("El reclamo cumple todos los criterios de evaluación. Monto aprobado: $" + montoAprobado + ".")
                    .banderas(null)
                    .build();
        }

        // Persistir log de auditoría
        evaluacionRepository.save(Evaluacion.builder()
                .reclamoId(request.getReclamoId())
                .numeroPoliza(request.getNumeroPoliza())
                .montoEstimado(request.getMontoEstimado())
                .valorAsegurado(request.getValorAsegurado())
                .deducibleAplicado(resultado.getDeducibleAplicado())
                .montoAprobado(resultado.getMontoAprobado())
                .estado(resultado.getEstado())
                .justificacion(resultado.getJustificacion())
                .fechaEvaluacion(LocalDateTime.now())
                .build());

        return resultado;
    }

    /**
     * deducible = MAX(montoEstimado * 10%, valorAsegurado * 1%, $200.00)
     */
    public BigDecimal calcularDeducible(BigDecimal montoEstimado, BigDecimal valorAsegurado) {
        BigDecimal opcionA = montoEstimado.multiply(new BigDecimal("0.10"));
        BigDecimal opcionB = valorAsegurado.multiply(new BigDecimal("0.01"));
        BigDecimal minimo  = new BigDecimal("200.00");
        return opcionA.max(opcionB).max(minimo);
    }
}
