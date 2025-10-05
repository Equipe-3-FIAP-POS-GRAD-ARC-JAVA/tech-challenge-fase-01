package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject;

import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

/**
 * Value Object representando um endereço de email.
 * 
 * Seguindo os princípios de DDD:
 * - Imutável (final fields, sem setters)
 * - Auto-validável (valida no construtor)
 * - Semântico (representa um conceito do domínio)
 * - Comparável por valor (equals/hashCode baseado no valor)
 */
public final class Email {

    private static final int MAX_LENGTH = 255;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final String value;

    /**
     * Construtor privado. Use o método of() para criar instâncias.
     */
    private Email(String value) {
        this.value = value;
    }

    /**
     * Factory method para criar um Email.
     * Valida e normaliza o email.
     * 
     * @param email Email em formato String
     * @return Instância de Email validada
     * @throws InvalidFieldException se o email for inválido
     */
    public static Email of(String email) {
        validate(email);
        String normalized = normalize(email);
        return new Email(normalized);
    }

    /**
     * Valida o formato do email.
     */
    private static void validate(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidFieldException("email", "Email é obrigatório");
        }

        String trimmed = email.trim();

        if (trimmed.length() > MAX_LENGTH) {
            throw new InvalidFieldException("email",
                    String.format("Email deve ter no máximo %d caracteres", MAX_LENGTH));
        }

        if (!trimmed.matches(EMAIL_PATTERN)) {
            throw new InvalidFieldException("email", "Email inválido. Use o formato: usuario@dominio.com");
        }
    }

    /**
     * Normaliza o email (trim + lowercase).
     */
    private static String normalize(String email) {
        return email.trim().toLowerCase();
    }

    /**
     * Retorna o valor do email como String.
     */
    public String getValue() {
        return value;
    }

    /**
     * Verifica se este email pertence a um domínio específico.
     * 
     * @param domain Domínio a verificar (ex: "example.com")
     * @return true se o email pertence ao domínio
     */
    public boolean belongsToDomain(String domain) {
        if (domain == null || domain.isBlank()) {
            return false;
        }
        return value.endsWith("@" + domain.toLowerCase());
    }

    /**
     * Retorna a parte local do email (antes do @).
     */
    public String getLocalPart() {
        int atIndex = value.indexOf('@');
        return atIndex > 0 ? value.substring(0, atIndex) : value;
    }

    /**
     * Retorna o domínio do email (depois do @).
     */
    public String getDomain() {
        int atIndex = value.indexOf('@');
        return atIndex > 0 && atIndex < value.length() - 1 ? value.substring(atIndex + 1) : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
