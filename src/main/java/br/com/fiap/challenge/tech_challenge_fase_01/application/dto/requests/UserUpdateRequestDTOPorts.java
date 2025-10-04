package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public record UserUpdateRequestDTOPorts(
                String id,
                String name,
                String email,
                String login) {

        public UserDomain toDomain(UserCreateRequestDTOPorts userCreateRequestDTO) {
                return UserDomain.builder()
                                .name(userCreateRequestDTO.name())
                                .email(userCreateRequestDTO.email())
                                .login(userCreateRequestDTO.login())
                                .password(userCreateRequestDTO.password())
                                .build();
        }

        public UserDomain toDomain(UserUpdateRequestDTOPorts userCreateRequestDTO) {
                return UserDomain.builder()
                                .name(userCreateRequestDTO.name())
                                .email(userCreateRequestDTO.email())
                                .login(userCreateRequestDTO.login())
                                .build();
        }

}
