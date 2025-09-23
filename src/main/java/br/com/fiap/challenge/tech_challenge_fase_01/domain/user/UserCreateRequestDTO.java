package br.com.fiap.challenge.tech_challenge_fase_01.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

public record UserCreateRequestDTO(
    @NotBlank
    @NotNull
    String name,

    @NotBlank
    @NotNull
    @Email
    String email,

    @NotBlank
    @NotNull
    String login,
    
    @NotBlank
    @NotNull
    String password
) {

    public static UserCreateRequestDTO from(User user) {
        return new UserCreateRequestDTO(user.getName(), user.getEmail(), user.getLogin(), user.getPassword());
    }

}
