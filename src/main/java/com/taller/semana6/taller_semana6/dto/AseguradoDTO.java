package com.taller.semana6.taller_semana6.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AseguradoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El numero de identificacion es obligatorio")
    private String numeroIdentificacion;

    private String direccion;

    private String telefono;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "Debe ser un correo electronico valido")
    private String correoElectronico;
}
