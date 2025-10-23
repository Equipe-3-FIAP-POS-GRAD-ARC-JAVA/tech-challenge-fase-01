package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(
    UUID id, 
    String email, 
    List<String> roles) {
}
