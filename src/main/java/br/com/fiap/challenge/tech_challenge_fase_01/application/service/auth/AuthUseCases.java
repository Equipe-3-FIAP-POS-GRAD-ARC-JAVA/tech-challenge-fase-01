package br.com.fiap.challenge.tech_challenge_fase_01.application.service.auth;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.JwtUtil;

/**
 * Use Case de Autenticação.
 * 
 * Responsabilidades (SOLID - SRP):
 * - Validar credenciais do usuário
 * - Verificar se usuário está ativo
 * - Gerar token JWT
 * 
 * Arquitetura Hexagonal:
 * - Use Case da camada de aplicação (core)
 * - Implementa AuthPort (port inbound)
 * - Usa UserRepositoryPort e PasswordEncoderPort (ports outbound)
 */
public class AuthUseCases implements AuthPort {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder; 
    private final JwtUtil jwtUtil;

    /**
     * Construtor para injeção de dependências.
     * 
     * @param userRepository Port para acesso aos dados de usuário
     * @param passwordEncoder Port para validação de senhas
     * @param jwtUtil Utilitário para geração de tokens JWT
     */
    public AuthUseCases(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest login) {
        // 1. Buscar usuário por username ou email (case-insensitive)
        String normalizedLogin = login.login().trim().toLowerCase();
        var userByLogin = userRepository.findByLogin(normalizedLogin);
        var userByEmail = userRepository.findByEmail(normalizedLogin);

        UserDomain user = userByLogin.or(() -> userByEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // 2. Verificar se usuário está ativo
        if (!user.isActive()) {
            throw new UserNotFoundException("Usuário inativo");
        }

        // 3. Validar senha usando BCrypt
        if (!passwordEncoder.matches(login.password(), user.getPassword())) {
            throw new UserNotFoundException("Credenciais inválidas");
        }

        // 4. Gerar token JWT
        String token = jwtUtil.generateToken(user.getLogin());

        return new LoginResponse(token);
    }
}
