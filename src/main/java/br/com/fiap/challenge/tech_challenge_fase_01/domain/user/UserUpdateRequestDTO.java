package br.com.fiap.challenge.tech_challenge_fase_01.domain.user;

import jakarta.validation.constraints.Email;

public record UserUpdateRequestDTO(
    String name,
    @Email
    String email,
    String login
) {

    public static UserUpdateRequestDTO from(User user) {
        return new UserUpdateRequestDTO(user.getName(), user.getEmail(), user.getLogin());
    }

}
