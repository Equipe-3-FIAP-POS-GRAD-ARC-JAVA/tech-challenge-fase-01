package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject;

import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

/**
 * Value Object representando um nome de usuário.
 * 
 * Seguindo os princípios de DDD:
 * - Imutável (final fields, sem setters)
 * - Auto-validável (valida no construtor)
 * - Semântico (representa um conceito do domínio)
 * - Comparável por valor (equals/hashCode baseado no valor)
 */
public final class Username {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]+$";

    private final String value;

    /**
     * Construtor privado. Use o método of() para criar instâncias.
     */
    private Username(String value) {
        this.value = value;
    }

    /**
     * Factory method para criar um Username.
     * Valida e normaliza o username.
     * 
     * @param username Username em formato String
     * @return Instância de Username validada
     * @throws InvalidFieldException se o username for inválido
     */
    public static Username of(String username) {
        validate(username);
        String normalized = normalize(username);
        return new Username(normalized);
    }

    /**
     * Valida o formato do username.
     */
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

        // Valida que não começa ou termina com caracteres especiais
        char firstChar = trimmed.charAt(0);
        char lastChar = trimmed.charAt(trimmed.length() - 1);

        if (!Character.isLetterOrDigit(firstChar) || !Character.isLetterOrDigit(lastChar)) {
            throw new InvalidFieldException("login",
                    "Login deve começar e terminar com letra ou número");
        }
    }

    /**
     * Normaliza o username (trim + lowercase).
     */
    private static String normalize(String username) {
        return username.trim().toLowerCase();
    }

    /**
     * Retorna o valor do username como String.
     */
    public String getValue() {
        return value;
    }

    /**
     * Verifica se o username contém apenas números.
     */
    public boolean isNumericOnly() {
        return value.matches("^\\d+$");
    }

    /**
     * Verifica se o username contém caracteres especiais.
     */
    public boolean hasSpecialCharacters() {
        return value.matches(".*[._-].*");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
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
