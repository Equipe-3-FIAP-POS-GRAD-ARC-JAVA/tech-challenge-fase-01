package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public record UserCreateRequest(
                String name,
                String email,
                String login,
                String password) {

        public UserDomain toDomain(UserCreateRequest userCreateRequest) {
                return UserDomain.builder()
                                .name(userCreateRequest.name())
                                .email(userCreateRequest.email())
                                .login(userCreateRequest.login())
                                .password(userCreateRequest.password())
                                .build();
        }

}
