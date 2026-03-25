package com.taller.semana6.taller_semana6.client;

import com.taller.semana6.taller_semana6.dto.AuthRegisterRequestDTO;

public interface AuthServiceClient {
    void registrarUsuario(AuthRegisterRequestDTO request);
}
