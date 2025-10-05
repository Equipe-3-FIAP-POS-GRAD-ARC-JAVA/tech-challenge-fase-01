package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

/**
 * DTO para resposta de usuário.
 * Usado na camada de aplicação (ports inbound).
 */
public record UserResponse(String id, String name, String email, String login) {

    public static UserResponse fromDomain(final UserDomain userDomain) {
        return new UserResponse(
                userDomain.getId(),
                userDomain.getName(),
                userDomain.getEmail(),
                userDomain.getLogin());
    }

}
