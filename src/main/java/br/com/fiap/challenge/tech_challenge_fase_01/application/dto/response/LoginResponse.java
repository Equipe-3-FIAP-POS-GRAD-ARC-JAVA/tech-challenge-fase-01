package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public record LoginResponse(
        String token,
        UserDomain user) {

}