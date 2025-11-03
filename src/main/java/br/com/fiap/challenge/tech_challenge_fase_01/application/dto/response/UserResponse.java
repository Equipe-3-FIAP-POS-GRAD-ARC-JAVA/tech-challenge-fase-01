package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

import java.util.List;
import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;

public record UserResponse(
    UUID id, 
    String name, 
    String email, 
    String login, 
    List<RolesEnum> roles,
    List<AddressResponse> addresses
) {
}