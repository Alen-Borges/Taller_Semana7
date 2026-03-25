package com.taller.semana7.msevaluacion.rules;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;
import com.taller.semana7.msevaluacion.service.rules.DeducibleRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DeducibleRuleTest {

    private DeducibleRule rule;

    @BeforeEach
    void setUp() {
        rule = new DeducibleRule();
    }

    @Test
    @DisplayName("Retorna DESCARTADO cuando monto < deducible")
    void retornaDescartado_cuandoMontoMenorQueDeducible() {
        EvaluacionRequestDTO req = buildRequest("150.00");
        Optional<EvaluacionResponseDTO> result = rule.evaluate(req, new BigDecimal("250.00"));

        assertThat(result).isPresent();
        assertThat(result.get().getEstado()).isEqualTo("DESCARTADO");
        assertThat(result.get().getMontoAprobado()).isNull();
    }

    @Test
    @DisplayName("Retorna DESCARTADO cuando monto = deducible")
    void retornaDescartado_cuandoMontoIgualADeducible() {
        EvaluacionRequestDTO req = buildRequest("250.00");
        Optional<EvaluacionResponseDTO> result = rule.evaluate(req, new BigDecimal("250.00"));

        assertThat(result).isPresent();
        assertThat(result.get().getEstado()).isEqualTo("DESCARTADO");
    }

    @Test
    @DisplayName("Retorna empty cuando monto > deducible (regla no aplica)")
    void retornaEmpty_cuandoMontoMayorQueDeducible() {
        EvaluacionRequestDTO req = buildRequest("500.00");
        Optional<EvaluacionResponseDTO> result = rule.evaluate(req, new BigDecimal("250.00"));

        assertThat(result).isEmpty();
    }

    private EvaluacionRequestDTO buildRequest(String monto) {
        EvaluacionRequestDTO dto = new EvaluacionRequestDTO();
        dto.setReclamoId(1L);
        dto.setMontoEstimado(new BigDecimal(monto));
        dto.setValorAsegurado(new BigDecimal("25000.00"));
        return dto;
    }
}
