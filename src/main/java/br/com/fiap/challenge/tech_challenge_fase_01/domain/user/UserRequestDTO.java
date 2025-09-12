package br.com.fiap.challenge.tech_challenge_fase_01.domain.user;

public record UserRequestDTO(
    String name,
    String email,
    String login,
    String password
) {

    public static UserRequestDTO from(User user) {
        return new UserRequestDTO(user.getName(), user.getEmail(), user.getLogin(), user.getPassword());
    }

}
