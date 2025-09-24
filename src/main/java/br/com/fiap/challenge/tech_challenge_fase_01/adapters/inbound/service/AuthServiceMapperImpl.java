package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.service;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.service.JwtTokenServiceImpl;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.inbound.AuthServiceMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.UserUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceMapperImpl implements AuthServiceMapper {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenServiceImpl jwtTokenService;
    private final UserUseCases userService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        final var authToken = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        authenticationManager.authenticate(authToken);
        return jwtTokenService.getLoginResponse(request.login());
    }

    @Override
    @Transactional(readOnly = true)
    public User findAuthenticatedUserByUsername(String username) {
        return userService.findByUsername(username);
    }
}
