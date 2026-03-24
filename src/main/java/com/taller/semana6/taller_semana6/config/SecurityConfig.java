package com.taller.semana6.taller_semana6.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/api/v1/asegurados/**").permitAll()
                        .requestMatchers("/api/v1/vehiculos/**").permitAll()
                        .requestMatchers("/api/v1/polizas/**").permitAll()
                        .anyRequest().authenticated()

                );

        return http.build();
    }
}
