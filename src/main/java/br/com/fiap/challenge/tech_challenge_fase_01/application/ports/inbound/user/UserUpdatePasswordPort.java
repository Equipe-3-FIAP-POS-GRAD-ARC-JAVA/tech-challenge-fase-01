package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;

public interface UserUpdatePasswordPort {

    public UserResponseDTOPorts updatePassword(
            String id,
            UpdatePasswordRequestDTOPorts updatePasswordRequestDTO);

}
