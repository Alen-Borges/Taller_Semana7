package com.taller.semana7.msauthservice.service;

import com.taller.semana7.msauthservice.dto.LoginRequestDTO;
import com.taller.semana7.msauthservice.dto.LoginResponseDTO;
import com.taller.semana7.msauthservice.dto.RegisterRequestDTO;
import com.taller.semana7.msauthservice.entity.Usuario;
import com.taller.semana7.msauthservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {
        String credencial = request.getUsername();

        Usuario usuario = credencial.contains("@")
                ? usuarioRepository.findByEmail(credencial)
                        .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"))
                : usuarioRepository.findByUsername(credencial)
                        .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRol().name(), usuario.getAseguradoId());

        return LoginResponseDTO.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .expiresIn(jwtService.getExpiration())
                .build();
    }

    public void register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El username '" + request.getUsername() + "' ya está en uso");
        }
        if (request.getEmail() != null && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo '" + request.getEmail() + "' ya está en uso");
        }

        usuarioRepository.save(Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .rol(request.getRol())
                .aseguradoId(request.getAseguradoId())
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }
}
