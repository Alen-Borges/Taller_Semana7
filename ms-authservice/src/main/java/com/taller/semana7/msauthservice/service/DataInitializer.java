package com.taller.semana7.msauthservice.service;

import com.taller.semana7.msauthservice.entity.Rol;
import com.taller.semana7.msauthservice.entity.Usuario;
import com.taller.semana7.msauthservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea usuarios por defecto al iniciar si no existen.
 * Útil para pruebas y desarrollo.
 *
 *  gestor01 / gestor123  → GESTOR
 *  asegurado01 / aseg123 → ASEGURADO
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        crearSiNoExiste("gestor01", "gestor123", "gestor@mail.com", Rol.GESTOR, null);
        crearSiNoExiste("asegurado01", "aseg123", "asegurado@mail.com", Rol.ASEGURADO, 1L);
    }

    private void crearSiNoExiste(String username, String password, String email, Rol rol, Long aseguradoId) {
        if (!usuarioRepository.existsByUsername(username)) {
            usuarioRepository.save(Usuario.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .rol(rol)
                    .aseguradoId(aseguradoId)
                    .build());
            log.info("Usuario creado: {} ({}) email={} aseguradoId={}", username, rol, email, aseguradoId);
        }
    }
}
