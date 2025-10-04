package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public record UserResponseDTOPorts(String id, String name, String email, String login) {

    public static UserResponseDTOPorts fromDomain(
            final UserDomain userDomain) {
        return new UserResponseDTOPorts(
                userDomain.getId(),
                userDomain.getName(),
                userDomain.getEmail(),
                userDomain.getLogin());
    }

}
