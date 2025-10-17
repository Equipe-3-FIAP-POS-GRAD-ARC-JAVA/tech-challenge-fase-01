package br.com.fiap.challenge.tech_challenge_fase_01.application.service.auth;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.JwtUtil;

public class AuthUseCases implements AuthPort {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder; 
    private final JwtUtil jwtUtil;

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
        String normalizedLogin = login.login().trim().toLowerCase();
        var userByLogin = userRepository.findByLogin(normalizedLogin);
        var userByEmail = userRepository.findByEmail(normalizedLogin);

        UserDomain user = userByLogin.or(() -> userByEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!user.isActive()) {
            throw new UserNotFoundException("Usuário inativo");
        }

        if (!passwordEncoder.matches(login.password(), user.getPassword())) {
            throw new UserNotFoundException("Credenciais inválidas");
        }

        var roles = user.getRole().stream().map(Enum::name).toList();
        String token = jwtUtil.generateToken(user.getLogin(), roles);

        return new LoginResponse(token, user);
    }
}