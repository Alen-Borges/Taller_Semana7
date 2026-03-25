package com.taller.semana6.taller_semana6.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRegisterRequestDTO {
    private String username;
    private String password;
    private String email;
    private String rol;
}
