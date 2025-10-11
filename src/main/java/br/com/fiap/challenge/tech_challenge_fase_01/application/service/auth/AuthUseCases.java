package br.com.fiap.challenge.tech_challenge_fase_01.application.service.auth;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.JwtTokenPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;

/**
 * Use Case de Autenticação.
 * 
 * Responsabilidades (SOLID - SRP):
 * - Validar credenciais do usuário
 * - Verificar se usuário está ativo
 * - Gerar token JWT através do port
 * 
 * Arquitetura Hexagonal:
 * - Use Case da camada de aplicação (core)
 * - Implementa AuthPort (port inbound)
 * - Usa UserRepositoryPort, PasswordEncoderPort e JwtTokenPort (ports outbound)
 * - Não conhece detalhes de infraestrutura (JWT, Spring Security, etc)
 */
public class AuthUseCases implements AuthPort {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder; 
    private final JwtTokenPort jwtTokenPort;

    /**
     * Construtor para injeção de dependências.
     * 
     * @param userRepository Port para acesso aos dados de usuário
     * @param passwordEncoder Port para validação de senhas
     * @param jwtTokenPort Port para geração de tokens JWT
     */
    public AuthUseCases(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. Buscar usuário por username ou email (case-insensitive)
        String normalizedLogin = request.login().trim().toLowerCase();
        var userByUsername = userRepository.findByUsername(normalizedLogin);
        var userByEmail = userRepository.findByEmail(normalizedLogin);

        UserDomain user = userByUsername.or(() -> userByEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // 2. Verificar se usuário está ativo
        if (!user.isActive()) {
            throw new UserNotFoundException("Usuário inativo");
        }

        // 3. Validar senha usando BCrypt
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserNotFoundException("Credenciais inválidas");
        }

        // 4. Gerar token JWT
        String token = jwtTokenPort.generateToken(user);

        return new LoginResponse(token);
    }
}
