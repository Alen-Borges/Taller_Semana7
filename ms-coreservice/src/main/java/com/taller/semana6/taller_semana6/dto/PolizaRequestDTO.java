package com.taller.semana6.taller_semana6.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolizaRequestDTO {

    @NotBlank(message = "El numero de poliza es obligatorio")
    private String numero;

    @NotNull(message = "El ID del asegurado es obligatorio")
    private Long aseguradoId;

    @NotNull(message = "El ID del vehiculo es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El valor asegurado es obligatorio")
    @Positive(message = "El valor asegurado debe ser un numero positivo")
    private BigDecimal valorAsegurado;

    @NotNull(message = "La fecha de inicio de vigencia es obligatoria")
    private LocalDate vigenciaInicio;

    @NotNull(message = "La fecha de fin de vigencia es obligatoria")
    @Future(message = "La fecha de fin de vigencia debe ser en el futuro")
    private LocalDate vigenciaFin;
}
