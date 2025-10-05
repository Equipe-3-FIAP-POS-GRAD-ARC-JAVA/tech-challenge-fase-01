package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

/**
 * DTO para requisição de criação de usuário.
 * Usado na camada de aplicação (ports inbound).
 */
public record UserCreateRequest(
                String name,
                String email,
                String login,
                String password) {

        public UserDomain toDomain() {
                return UserDomain.builder()
                                .name(this.name)
                                .email(this.email)
                                .login(this.login)
                                .password(this.password)
                                .build();
        }

}
