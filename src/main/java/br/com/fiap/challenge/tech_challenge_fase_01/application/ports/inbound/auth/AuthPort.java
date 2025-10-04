package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;

public interface AuthPort {
    UserResponseDTOPorts login(LoginRequestDTOPorts request);
}
