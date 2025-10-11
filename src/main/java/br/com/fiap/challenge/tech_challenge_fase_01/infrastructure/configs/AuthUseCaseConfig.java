package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.JwtTokenPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.service.auth.AuthUseCases;

/**
 * Configuração de beans dos Use Cases de autenticação.
 *
 * Responsabilidades (SOLID - SRP):
 * - Instanciar e configurar Use Cases relacionados a autenticação
 * - Resolver dependências entre Use Cases de auth e Ports
 *
 * Arquitetura Hexagonal:
 * - Configuração da camada de aplicação para operações de autenticação
 * - Liga Ports inbound de autenticação com suas implementações (Use Cases)
 * - Mantém dependências explícitas e testáveis
 */
@Configuration
public class AuthUseCaseConfig {

    /**
     * Bean do Use Case de autenticação.
     * Responsável por validar credenciais de usuário e gerar tokens JWT.
     *
     * Funcionalidades:
     * - Validação de credenciais (username/password)
     * - Geração de tokens JWT para sessões autenticadas
     * - Verificação de integridade do token
     *
     * @param userRepository porta para acesso ao repositório de usuários
     * @param passwordEncoder porta para validação de senhas
     * @param jwtTokenPort porta para operações com tokens JWT
     * @return implementação do AuthPort
     */
    @Bean
    public AuthPort authPort(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            JwtTokenPort jwtTokenPort) {
        return new AuthUseCases(userRepository, passwordEncoder, jwtTokenPort);
    }
}