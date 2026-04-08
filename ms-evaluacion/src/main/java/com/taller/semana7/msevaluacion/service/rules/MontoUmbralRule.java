package com.taller.semana7.msevaluacion.service.rules;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Regla R3: Escalamiento por monto umbral.
 *
 * Condición: montoEstimado >= 20% del valorAsegurado
 * Resultado: EN_REVISION_MANUAL — el reclamo excede el umbral máximo aprobable automáticamente.
 *
 * Ejecutada en orden 2 (segunda del chain, después de DeducibleRule).
 */
@Component
@Order(2)
public class MontoUmbralRule implements EvaluacionRule {

    private static final BigDecimal UMBRAL_PORCENTAJE = new BigDecimal("0.20");

    @Override
    public Optional<EvaluacionResponseDTO> evaluate(EvaluacionRequestDTO request, BigDecimal deducible) {
        BigDecimal umbral = request.getValorAsegurado().multiply(UMBRAL_PORCENTAJE);

        if (request.getMontoEstimado().compareTo(umbral) >= 0) {
            return Optional.of(EvaluacionResponseDTO.builder()
                    .estado("EN_REVISION_MANUAL")
                    .montoAprobado(null)
                    .deducibleAplicado(deducible)
                    .justificacion("El monto estimado ($" + request.getMontoEstimado()
                            + ") supera el 20% del valor asegurado ($" + umbral + "). Se escala a revisión manual.")
                    .banderas(List.of("MONTO_SUPERA_UMBRAL_20PCT"))
                    .build());
        }
        return Optional.empty();
    }
}
