package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public record UserUpdateRequest(
                String id,
                String name,
                String email,
                String login) {

        public UserDomain toDomain(UserUpdateRequest userCreateRequest) {
                return UserDomain.builder()
                                .name(userCreateRequest.name())
                                .email(userCreateRequest.email())
                                .login(userCreateRequest.login())
                                .build();
        }

}
