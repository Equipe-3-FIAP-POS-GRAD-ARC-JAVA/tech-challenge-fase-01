package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.service.auth.AuthUseCases;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.JwtUtil;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public AuthPort authPort(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            JwtUtil jwtUtil) {
        return new AuthUseCases(userRepository, passwordEncoder, jwtUtil);
    }
}
