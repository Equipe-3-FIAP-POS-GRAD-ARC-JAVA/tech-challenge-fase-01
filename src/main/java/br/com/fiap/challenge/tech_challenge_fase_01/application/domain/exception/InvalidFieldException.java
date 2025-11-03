package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception;

public class InvalidFieldException extends DomainValidationException {

    private final String fieldName;

    public InvalidFieldException(String fieldName, String message) {
        super(String.format("Campo '%s': %s", fieldName, message));
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}