package br.com.fiap.challenge.tech_challenge_fase_01.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;

public class UserMapper {

    public static UserResponse toResponse(UserDomain userDomain) {
        if (userDomain == null) {
            return null;
        }

        return new UserResponse(
                userDomain.getId(),
                userDomain.getName(),
                userDomain.getEmail(),
                userDomain.getLogin());
    }

    public static List<UserResponse> toResponseList(List<UserDomain> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class - não deve ser instanciada");
    }
}