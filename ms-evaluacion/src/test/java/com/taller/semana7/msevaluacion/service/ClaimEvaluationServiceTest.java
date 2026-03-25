package com.taller.semana7.msevaluacion.service;

import com.taller.semana7.msevaluacion.dto.EvaluacionRequestDTO;
import com.taller.semana7.msevaluacion.dto.EvaluacionResponseDTO;
import com.taller.semana7.msevaluacion.entity.Evaluacion;
import com.taller.semana7.msevaluacion.repository.EvaluacionRepository;
import com.taller.semana7.msevaluacion.service.rules.DeducibleRule;
import com.taller.semana7.msevaluacion.service.rules.MontoUmbralRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

/**
 * Tests de los casos de prueba CP001–CP004 definidos en §6.4.4
 * de las Especificaciones Técnicas MVP v2.
 *
 * Configuración base: valorAsegurado = $25,000.00
 *   deducible = MAX(monto*10%, 25000*1%=250, 200)
 *   umbral 20% = $5,000.00
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClaimEvaluationServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    private ClaimEvaluationService service;

    @BeforeEach
    void setUp() {
        lenient().when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(inv -> inv.getArgument(0));
        service = new ClaimEvaluationService(
                List.of(new DeducibleRule(), new MontoUmbralRule()),
                evaluacionRepository
        );
    }

    // ─── CP001 ────────────────────────────────────────────────────────────────
    // monto=$150 < deducible=MAX(15, 250, 200)=$250 → DESCARTADO

    @Test
    @DisplayName("CP001 — monto menor que deducible → DESCARTADO")
    void cp001_montoMenorQueDeducible_retornaDescartado() {
        EvaluacionRequestDTO request = buildRequest("150.00", "25000.00");

        EvaluacionResponseDTO response = service.evaluar(request);

        assertThat(response.getEstado()).isEqualTo("DESCARTADO");
        assertThat(response.getDeducibleAplicado()).isEqualByComparingTo("250.00");
        assertThat(response.getMontoAprobado()).isNull();
    }

    // ─── CP002 ────────────────────────────────────────────────────────────────
    // monto=$250 = deducible=MAX(25, 250, 200)=$250 → DESCARTADO (igual también descarta)

    @Test
    @DisplayName("CP002 — monto igual al deducible → DESCARTADO")
    void cp002_montoIgualADeducible_retornaDescartado() {
        EvaluacionRequestDTO request = buildRequest("250.00", "25000.00");

        EvaluacionResponseDTO response = service.evaluar(request);

        assertThat(response.getEstado()).isEqualTo("DESCARTADO");
        assertThat(response.getDeducibleAplicado()).isEqualByComparingTo("250.00");
        assertThat(response.getMontoAprobado()).isNull();
    }

    // ─── CP003 ────────────────────────────────────────────────────────────────
    // monto=$3500 > deducible=MAX(350, 250, 200)=$350
    // monto=$3500 < umbral=25000*20%=$5000 → APROBADO
    // montoAprobado = 3500 - 350 = $3150

    @Test
    @DisplayName("CP003 — monto supera deducible y está bajo umbral → APROBADO con $3,150")
    void cp003_montoSuperaDeducibleYBajoUmbral_retornaAprobado() {
        EvaluacionRequestDTO request = buildRequest("3500.00", "25000.00");

        EvaluacionResponseDTO response = service.evaluar(request);

        assertThat(response.getEstado()).isEqualTo("APROBADO");
        assertThat(response.getDeducibleAplicado()).isEqualByComparingTo("350.00");
        assertThat(response.getMontoAprobado()).isEqualByComparingTo("3150.00");
    }

    // ─── CP004 ────────────────────────────────────────────────────────────────
    // monto=$6000 >= umbral=25000*20%=$5000 → EN_REVISION_MANUAL

    @Test
    @DisplayName("CP004 — monto supera el 20% del valor asegurado → EN_REVISION_MANUAL")
    void cp004_montoSuperaUmbral_retornaEnRevisionManual() {
        EvaluacionRequestDTO request = buildRequest("6000.00", "25000.00");

        EvaluacionResponseDTO response = service.evaluar(request);

        assertThat(response.getEstado()).isEqualTo("EN_REVISION_MANUAL");
        assertThat(response.getDeducibleAplicado()).isEqualByComparingTo("600.00");
        assertThat(response.getMontoAprobado()).isNull();
        assertThat(response.getBanderas()).contains("MONTO_SUPERA_UMBRAL_20PCT");
    }

    // ─── Verificación del cálculo de deducible ────────────────────────────────

    @Test
    @DisplayName("calcularDeducible aplica el máximo de las tres opciones")
    void calcularDeducible_retornaMaximoDeTresOpciones() {
        // opcionA = 1000*10% = 100, opcionB = 5000*1% = 50, minimo = 200 → MAX = 200
        assertThat(service.calcularDeducible(new BigDecimal("1000"), new BigDecimal("5000")))
                .isEqualByComparingTo("200.00");

        // opcionA = 3000*10% = 300, opcionB = 10000*1% = 100, minimo = 200 → MAX = 300
        assertThat(service.calcularDeducible(new BigDecimal("3000"), new BigDecimal("10000")))
                .isEqualByComparingTo("300.00");

        // opcionA = 100*10% = 10, opcionB = 50000*1% = 500, minimo = 200 → MAX = 500
        assertThat(service.calcularDeducible(new BigDecimal("100"), new BigDecimal("50000")))
                .isEqualByComparingTo("500.00");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private EvaluacionRequestDTO buildRequest(String monto, String valorAsegurado) {
        EvaluacionRequestDTO dto = new EvaluacionRequestDTO();
        dto.setReclamoId(1L);
        dto.setNumeroPoliza("POL-TEST-001");
        dto.setMontoEstimado(new BigDecimal(monto));
        dto.setValorAsegurado(new BigDecimal(valorAsegurado));
        return dto;
    }
}
