package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject;

import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

public final class Username {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]+$";

    private final String value;

    private Username(String value) {
        this.value = value;
    }

    public static Username of(String username) {
        validate(username);
        String normalized = normalize(username);
        return new Username(normalized);
    }

    private static void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidFieldException("login", "Login é obrigatório");
        }

        String trimmed = username.trim();

        if (trimmed.length() < MIN_LENGTH) {
            throw new InvalidFieldException("login",
                    String.format("Login deve ter no mínimo %d caracteres", MIN_LENGTH));
        }

        if (trimmed.length() > MAX_LENGTH) {
            throw new InvalidFieldException("login",
                    String.format("Login deve ter no máximo %d caracteres", MAX_LENGTH));
        }

        if (!trimmed.matches(USERNAME_PATTERN)) {
            throw new InvalidFieldException("login",
                    "Login deve conter apenas letras, números e caracteres especiais (. _ -)");
        }

        char firstChar = trimmed.charAt(0);
        char lastChar = trimmed.charAt(trimmed.length() - 1);

        if (!Character.isLetterOrDigit(firstChar) || !Character.isLetterOrDigit(lastChar)) {
            throw new InvalidFieldException("login",
                    "Login deve começar e terminar com letra ou número");
        }
    }

    private static String normalize(String username) {
        return username.trim().toLowerCase();
    }

    public String getValue() {
        return value;
    }

    public boolean isNumericOnly() {
        return value.matches("^\\d+$");
    }

    public boolean hasSpecialCharacters() {
        return value.matches(".*[._-].*");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return Objects.equals(value, username.value);
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