package br.com.fiap.challenge.tech_challenge_fase_01.domain.user;

public record UserCreateRequestDTO(
    String name,
    String email,
    String login,
    String password
) {

    public static UserCreateRequestDTO from(User user) {
        return new UserCreateRequestDTO(user.getName(), user.getEmail(), user.getLogin(), user.getPassword());
    }

}
