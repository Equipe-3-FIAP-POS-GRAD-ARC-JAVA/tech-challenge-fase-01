package br.com.fiap.challenge.tech_challenge_fase_01.domain.user;

public record UserResponseDTO(
    String id,
    String name,
    String email,
    String login
) {

    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getLogin());
    }

}
