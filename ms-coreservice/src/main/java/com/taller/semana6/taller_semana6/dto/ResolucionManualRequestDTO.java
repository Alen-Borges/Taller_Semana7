package com.taller.semana6.taller_semana6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request del gestor para resolver manualmente un reclamo escalado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolucionManualRequestDTO {

    @NotNull(message = "La decisión es obligatoria (APROBADO o RECHAZADO)")
    private DecisionManual decision;

    @NotBlank(message = "La justificación es obligatoria")
    @Size(min = 10, max = 2000, message = "La justificación debe tener entre 10 y 2000 caracteres")
    private String justificacion;

    public enum DecisionManual {
        APROBADO, RECHAZADO
    }
}
