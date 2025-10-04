package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;

public interface AuthServicePort {
    UserResponseDTOPorts login(LoginRequestDTOPorts request);

    UserResponseDTOPorts findAuthenticatedUserByUsername(String username);
}
