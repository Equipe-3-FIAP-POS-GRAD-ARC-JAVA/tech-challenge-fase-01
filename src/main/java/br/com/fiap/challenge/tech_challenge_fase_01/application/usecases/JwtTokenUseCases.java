package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.AuthServicePort;

public class JwtTokenUseCases implements AuthServicePort {
    
    @Override
    public UserResponseDTOPorts login(LoginRequestDTOPorts request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserResponseDTOPorts findAuthenticatedUserByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
