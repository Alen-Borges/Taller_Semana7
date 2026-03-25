package com.taller.semana7.msauthservice.service;

import com.taller.semana7.msauthservice.dto.LoginRequestDTO;
import com.taller.semana7.msauthservice.dto.LoginResponseDTO;
import com.taller.semana7.msauthservice.dto.RegisterRequestDTO;
import com.taller.semana7.msauthservice.entity.Rol;
import com.taller.semana7.msauthservice.entity.Usuario;
import com.taller.semana7.msauthservice.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "c2VndXJvc0V2YWx1YWRvckF1dG9tYXRpemFkb1NlY3JldEtleUZvckpXVDIwMjY=");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);

        authService = new AuthService(usuarioRepository, jwtService, passwordEncoder);
    }

    // ─── Login ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Login correcto → devuelve token con username y rol")
    void login_credencialesCorrectas_retornaToken() {
        Usuario usuario = Usuario.builder()
                .username("gestor01")
                .password(passwordEncoder.encode("gestor123"))
                .rol(Rol.GESTOR)
                .build();
        when(usuarioRepository.findByUsername("gestor01")).thenReturn(Optional.of(usuario));

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("gestor01");
        request.setPassword("gestor123");

        LoginResponseDTO response = authService.login(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getUsername()).isEqualTo("gestor01");
        assertThat(response.getRol()).isEqualTo("GESTOR");
        assertThat(response.getExpiresIn()).isEqualTo(86400000L);
    }

    @Test
    @DisplayName("Login con username inexistente → BadCredentialsException")
    void login_usuarioNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("noexiste");
        request.setPassword("cualquiera");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("Login con contraseña incorrecta → BadCredentialsException")
    void login_passwordIncorrecta_lanzaExcepcion() {
        Usuario usuario = Usuario.builder()
                .username("asegurado01")
                .password(passwordEncoder.encode("aseg123"))
                .rol(Rol.ASEGURADO)
                .build();
        when(usuarioRepository.findByUsername("asegurado01")).thenReturn(Optional.of(usuario));

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("asegurado01");
        request.setPassword("wrongpassword");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    // ─── Register ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registro correcto → guarda usuario con password encriptado")
    void register_nuevoUsuario_guardaConPasswordEncriptado() {
        when(usuarioRepository.existsByUsername("nuevo")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("nuevo");
        request.setPassword("pass123");
        request.setRol(Rol.ASEGURADO);

        authService.register(request);

        verify(usuarioRepository).save(argThat(u ->
                u.getUsername().equals("nuevo") &&
                passwordEncoder.matches("pass123", u.getPassword()) &&
                u.getRol() == Rol.ASEGURADO
        ));
    }

    @Test
    @DisplayName("Registro con username duplicado → IllegalArgumentException")
    void register_usernameDuplicado_lanzaExcepcion() {
        when(usuarioRepository.existsByUsername("gestor01")).thenReturn(true);

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("gestor01");
        request.setPassword("pass123");
        request.setRol(Rol.GESTOR);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("gestor01");
    }

    // ─── JWT claims ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Token generado contiene username y rol correctos")
    void login_tokenContieneClaimsCorrectos() {
        Usuario usuario = Usuario.builder()
                .username("gestor01")
                .password(passwordEncoder.encode("gestor123"))
                .rol(Rol.GESTOR)
                .build();
        when(usuarioRepository.findByUsername("gestor01")).thenReturn(Optional.of(usuario));

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("gestor01");
        request.setPassword("gestor123");

        LoginResponseDTO response = authService.login(request);

        assertThat(jwtService.extractUsername(response.getToken())).isEqualTo("gestor01");
        assertThat(jwtService.extractRol(response.getToken())).isEqualTo("GESTOR");
        assertThat(jwtService.isTokenValid(response.getToken())).isTrue();
    }
}
