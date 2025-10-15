package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<? > handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(401).body("Credenciais inválidas.");
    }

    @ExceptionHandler({ DisabledException.class, LockedException.class })
    public ResponseEntity<? > handleAccountIssues(RuntimeException ex) {
        return ResponseEntity.status(403).body("Usuário desabilitado ou bloqueado.");
    }
}
