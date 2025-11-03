package br.com.fiap.challenge.tech_challenge_fase_01.application.mapper;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;

public class UserMapper {

    public static UserResponse toResponse(UserDomain userDomain, List<AddressResponse> addresses) {
        if (userDomain == null) {
            return null;
        }

        return new UserResponse(
                userDomain.getId(),
                userDomain.getName(),
                userDomain.getEmail(),
                userDomain.getLogin(),
                userDomain.getRole() != null ? userDomain.getRole() : List.of(),
                addresses != null ? addresses : List.of());
    }

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class - não deve ser instanciada");
    }
}