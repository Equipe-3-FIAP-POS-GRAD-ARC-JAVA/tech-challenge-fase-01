package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception;

/**
 * Exceção base para validações de domínio.
 * 
 * Esta exceção é lançada quando uma regra de negócio ou invariante do domínio é
 * violada.
 * Representa um erro que deve ser tratado pela aplicação e comunicado ao
 * usuário.
 */
public class DomainValidationException extends RuntimeException {

    public DomainValidationException(String message) {
        super(message);
    }

    public DomainValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
