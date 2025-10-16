package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject;

import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

public final class Email {

    private static final int MAX_LENGTH = 255;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String email) {
        validate(email);
        String normalized = normalize(email);
        return new Email(normalized);
    }

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

    private static String normalize(String email) {
        return email.trim().toLowerCase();
    }

    public String getValue() {
        return value;
    }

    public boolean belongsToDomain(String domain) {
        if (domain == null || domain.isBlank()) {
            return false;
        }
        return value.endsWith("@" + domain.toLowerCase());
    }

    public String getLocalPart() {
        int atIndex = value.indexOf('@');
        return atIndex > 0 ? value.substring(0, atIndex) : value;
    }

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