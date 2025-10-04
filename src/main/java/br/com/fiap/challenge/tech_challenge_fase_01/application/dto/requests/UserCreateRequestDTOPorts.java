package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public record UserCreateRequestDTOPorts(
                String name,
                String email,
                String login,
                String password) {

        public UserDomain toDomain(UserCreateRequestDTOPorts userCreateRequestDTO) {
                return UserDomain.builder()
                                .name(userCreateRequestDTO.name())
                                .email(userCreateRequestDTO.email())
                                .login(userCreateRequestDTO.login())
                                .password(userCreateRequestDTO.password())
                                .build();
        }

}
