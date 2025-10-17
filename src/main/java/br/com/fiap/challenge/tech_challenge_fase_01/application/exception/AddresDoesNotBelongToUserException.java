package br.com.fiap.challenge.tech_challenge_fase_01.application.exception;

public class AddresDoesNotBelongToUserException extends RuntimeException {

    public AddresDoesNotBelongToUserException(String message) {
        super(message);
    }

    public AddresDoesNotBelongToUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
