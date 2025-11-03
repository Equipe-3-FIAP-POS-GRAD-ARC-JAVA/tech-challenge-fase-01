package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response;

import java.util.List;

public record UserResponseDTO(
    String id, 
    String name, 
    String email, 
    String login,
    List<String> roles,
    List<AddressResponseDTO> addresses
) {

}
