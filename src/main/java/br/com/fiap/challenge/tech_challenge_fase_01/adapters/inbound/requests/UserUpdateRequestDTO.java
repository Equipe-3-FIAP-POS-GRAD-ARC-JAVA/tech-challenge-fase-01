package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;

public record UserUpdateRequestDTO(
    String name,
    String email,
    String login
) {

    public static UserUpdateRequestDTO from(User user) {
        return new UserUpdateRequestDTO(user.getName(), user.getEmail(), user.getLogin());
    }

}
