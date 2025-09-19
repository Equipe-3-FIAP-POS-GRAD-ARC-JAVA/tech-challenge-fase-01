package br.com.fiap.challenge.tech_challenge_fase_01.application.service;


import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.dto.AuthenticatedUserDto;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.jwt.JwtTokenManager;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.JwtTokenUseCases;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.UserUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenUseCases {

    private final UserUseCases userUseCases;

    private final JwtTokenManager jwtTokenManager;

    private final UserMapper userMapper;

    public LoginResponse getLoginResponse(String login) {

        final AuthenticatedUserDto authenticatedUserDto = userUseCases.findAuthenticatedUserByUsername(login);

        //final User user = userMapper.toEntity(authenticatedUserDto);
        final String token = jwtTokenManager.generateToken(user);

        log.info("{} has successfully logged in!", user.getName());

        return new LoginResponse(token);
    }

}
