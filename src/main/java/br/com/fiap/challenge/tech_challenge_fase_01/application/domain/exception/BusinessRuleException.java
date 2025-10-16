package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception;

/**
 * Exceção lançada quando uma regra de negócio do domínio é violada.
 */
public class BusinessRuleException extends DomainValidationException {

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
