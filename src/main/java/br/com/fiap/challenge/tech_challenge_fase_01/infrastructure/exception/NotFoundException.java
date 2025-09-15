package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

    public NotFoundException(HttpStatusCode code, String message) {
        super(code, message);
    }

}
