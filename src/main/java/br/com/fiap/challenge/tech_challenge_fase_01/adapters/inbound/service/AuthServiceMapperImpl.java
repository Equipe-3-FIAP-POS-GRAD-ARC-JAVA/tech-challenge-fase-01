package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.service;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exceptions.UnauthorizedException;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.inbound.AuthServiceMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.JwtTokenUseCases;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.UserUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceMapperImpl implements AuthServiceMapper {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUseCases jwtTokenService;
    private final UserUseCases userService;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public LoginResponse login(@Valid LoginRequest request) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(request.login(), request.password());
            var authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenService.getLoginResponse(request.login());
        } catch (AuthenticationException ex) {
            // lance sua exceção de domínio mapeada para 401
            throw new UnauthorizedException("Credenciais inválidas.");
        }
    }

    @Override
    public User findAuthenticatedUserByUsername(String username) {
        // se a intenção é “pegar o logado”, prefira usar o SecurityContext:
        // var principal = SecurityContextHolder.getContext().getAuthentication().getName();
        // return userService.findByUsername(principal);
        return userService.findByUsername(username);
    }
}
