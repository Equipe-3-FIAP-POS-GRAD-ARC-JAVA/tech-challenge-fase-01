package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject;

import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

public final class PersonName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;
    private static final String NAME_PATTERN = "^[a-zA-ZÀ-ÿ\\s.'-]+$";

    private final String value;

    private PersonName(String value) {
        this.value = value;
    }

    public static PersonName of(String name) {
        validate(name);
        String normalized = normalize(name);
        return new PersonName(normalized);
    }

    private static void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidFieldException("name", "Nome é obrigatório");
        }

        String trimmed = name.trim();

        if (trimmed.length() < MIN_LENGTH) {
            throw new InvalidFieldException("name",
                    String.format("Nome deve ter no mínimo %d caracteres", MIN_LENGTH));
        }

        if (trimmed.length() > MAX_LENGTH) {
            throw new InvalidFieldException("name",
                    String.format("Nome deve ter no máximo %d caracteres", MAX_LENGTH));
        }

        if (!trimmed.matches(NAME_PATTERN)) {
            throw new InvalidFieldException("name",
                    "Nome deve conter apenas letras, espaços e caracteres especiais permitidos (. ' -)");
        }
    }

    private static String normalize(String name) {
        return name.trim();
    }

    public String getValue() {
        return value;
    }

    public String getFirstName() {
        String[] parts = value.split("\\s+");
        return parts.length > 0 ? parts[0] : value;
    }

    public String getLastName() {
        String[] parts = value.split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }

    public String getInitials() {
        String[] parts = value.split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return initials.toString();
    }

    public boolean isCompound() {
        return value.contains(" ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PersonName that = (PersonName) o;
        return Objects.equals(value, that.value);
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