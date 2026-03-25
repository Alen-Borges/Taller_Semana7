package com.taller.semana7.msevaluacion.service.rules;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Interfaz del patrón Strategy para las reglas del motor de evaluación.
 *
 * Cada regla recibe el request y el deducible ya calculado.
 * Si la regla aplica y es terminal, retorna un Optional con el resultado.
 * Si la regla no aplica, retorna Optional.empty() para pasar a la siguiente.
 *
 * Principio OCP: agregar una nueva regla = nueva clase que implemente esta interfaz,
 * sin modificar ClaimEvaluationService.
 */
public interface EvaluacionRule {

    Optional<EvaluacionResponseDTO> evaluate(EvaluacionRequestDTO request, BigDecimal deducible);
}
