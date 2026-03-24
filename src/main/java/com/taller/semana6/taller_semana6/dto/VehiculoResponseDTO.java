package com.taller.semana6.taller_semana6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoResponseDTO {
    private Long id;
    private String marca;
    private String modelo;
    private Integer anio;
    private String placa;
    
    private Long aseguradoId;
    private String aseguradoNombreCompleto;
}
