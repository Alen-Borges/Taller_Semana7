package com.taller.semana6.taller_semana6.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamoRequestDTO {

    @NotNull(message = "El ID de la póliza es obligatorio")
    private Long polizaId;

    @NotNull(message = "La fecha del incidente es obligatoria")
    @PastOrPresent(message = "La fecha del incidente no puede ser futura")
    private LocalDate fechaIncidente;

    @NotBlank(message = "La descripción del incidente es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El monto estimado es obligatorio")
    @Positive(message = "El monto estimado debe ser mayor a cero")
    private BigDecimal montoEstimado;

    @NotBlank(message = "La ubicación del incidente es obligatoria")
    private String ubicacion;

    // Fotografías (opcional al mínimo, validadas en servicio)
    private List<MultipartFile> fotografias;
}
