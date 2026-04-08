package com.taller.semana7.msevaluacion.service.rules;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Regla R1: Descarte por deducible.
 *
 * Condición: montoEstimado <= deducible
 * Resultado: DESCARTADO — el monto no supera el deducible mínimo.
 *
 * Ejecutada en orden 1 (primera del chain).
 */
@Component
@Order(1)
public class DeducibleRule implements EvaluacionRule {

    @Override
    public Optional<EvaluacionResponseDTO> evaluate(EvaluacionRequestDTO request, BigDecimal deducible) {
        if (request.getMontoEstimado().compareTo(deducible) <= 0) {
            return Optional.of(EvaluacionResponseDTO.builder()
                    .estado("DESCARTADO")
                    .montoAprobado(null)
                    .deducibleAplicado(deducible)
                    .justificacion("El monto estimado ($" + request.getMontoEstimado()
                            + ") no supera el deducible calculado ($" + deducible + ").")
                    .banderas(null)
                    .build());
        }
        return Optional.empty();
    }
}
