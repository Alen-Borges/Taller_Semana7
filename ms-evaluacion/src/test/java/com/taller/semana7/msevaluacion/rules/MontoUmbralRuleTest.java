package com.taller.semana7.msevaluacion.rules;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;
import com.taller.semana7.msevaluacion.service.rules.MontoUmbralRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MontoUmbralRuleTest {

    private MontoUmbralRule rule;

    @BeforeEach
    void setUp() {
        rule = new MontoUmbralRule();
    }

    @Test
    @DisplayName("Retorna EN_REVISION_MANUAL cuando monto >= 20% del valorAsegurado")
    void retornaEscalado_cuandoMontoSuperaUmbral() {
        EvaluacionRequestDTO req = buildRequest("6000.00", "25000.00");
        // umbral = 25000 * 20% = 5000; 6000 >= 5000

        Optional<EvaluacionResponseDTO> result = rule.evaluate(req, new BigDecimal("600.00"));

        assertThat(result).isPresent();
        assertThat(result.get().getEstado()).isEqualTo("EN_REVISION_MANUAL");
        assertThat(result.get().getBanderas()).contains("MONTO_SUPERA_UMBRAL_20PCT");
        assertThat(result.get().getMontoAprobado()).isNull();
    }

    @Test
    @DisplayName("Retorna EN_REVISION_MANUAL cuando monto = exactamente 20% del valorAsegurado")
    void retornaEscalado_cuandoMontoIgualAlUmbral() {
        EvaluacionRequestDTO req = buildRequest("5000.00", "25000.00");
        // umbral = 5000; 5000 >= 5000

        Optional<EvaluacionResponseDTO> result = rule.evaluate(req, new BigDecimal("500.00"));

        assertThat(result).isPresent();
        assertThat(result.get().getEstado()).isEqualTo("EN_REVISION_MANUAL");
    }

    @Test
    @DisplayName("Retorna empty cuando monto < 20% del valorAsegurado (regla no aplica)")
    void retornaEmpty_cuandoMontoBajoUmbral() {
        EvaluacionRequestDTO req = buildRequest("3500.00", "25000.00");
        // umbral = 5000; 3500 < 5000

        Optional<EvaluacionResponseDTO> result = rule.evaluate(req, new BigDecimal("350.00"));

        assertThat(result).isEmpty();
    }

    private EvaluacionRequestDTO buildRequest(String monto, String valorAsegurado) {
        EvaluacionRequestDTO dto = new EvaluacionRequestDTO();
        dto.setReclamoId(1L);
        dto.setMontoEstimado(new BigDecimal(monto));
        dto.setValorAsegurado(new BigDecimal(valorAsegurado));
        return dto;
    }
}
