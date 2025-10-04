package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;

public interface UserUpdatePort {

    public UserResponseDTOPorts update(
            String id,
            UserUpdateRequestDTOPorts userUpdateRequestDTO);

}
