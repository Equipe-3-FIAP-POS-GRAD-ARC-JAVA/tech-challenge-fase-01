package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;

/**
 * Adapter Outbound que implementa o port de codificação de senhas.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Adaptar o PasswordEncoder do Spring Security para o port da aplicação
 * - Delegar operações de hash de senha
 * 
 * Arquitetura Hexagonal:
 * - Adapter Outbound (driven adapter)
 * - Implementa PasswordEncoderPort (port outbound)
 * - Usa PasswordEncoder do Spring Security
 * - Mantém a camada de aplicação independente de Spring Security
 * 
 * OCP: Se precisar trocar BCrypt por outro algoritmo (Argon2, etc),
 * basta trocar o bean no SecurityBeansConfig, sem alterar este adapter.
 */
@Component
@RequiredArgsConstructor
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
