package com.taller.semana7.msauthservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    private String token;
    private String username;
    private String rol;
    private long expiresIn;   // milisegundos
}
