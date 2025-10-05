package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject;

import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

/**
 * Value Object representando um nome de pessoa.
 * 
 * Seguindo os princípios de DDD:
 * - Imutável (final fields, sem setters)
 * - Auto-validável (valida no construtor)
 * - Semântico (representa um conceito do domínio)
 * - Comparável por valor (equals/hashCode baseado no valor)
 */
public final class PersonName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;
    private static final String NAME_PATTERN = "^[a-zA-ZÀ-ÿ\\s.'-]+$";

    private final String value;

    /**
     * Construtor privado. Use o método of() para criar instâncias.
     */
    private PersonName(String value) {
        this.value = value;
    }

    /**
     * Factory method para criar um PersonName.
     * Valida e normaliza o nome.
     * 
     * @param name Nome em formato String
     * @return Instância de PersonName validada
     * @throws InvalidFieldException se o nome for inválido
     */
    public static PersonName of(String name) {
        validate(name);
        String normalized = normalize(name);
        return new PersonName(normalized);
    }

    /**
     * Valida o formato do nome.
     */
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

    /**
     * Normaliza o nome (trim + capitaliza primeira letra de cada palavra).
     */
    private static String normalize(String name) {
        return name.trim();
    }

    /**
     * Retorna o valor do nome como String.
     */
    public String getValue() {
        return value;
    }

    /**
     * Retorna o primeiro nome (primeira palavra).
     */
    public String getFirstName() {
        String[] parts = value.split("\\s+");
        return parts.length > 0 ? parts[0] : value;
    }

    /**
     * Retorna o sobrenome (última palavra).
     */
    public String getLastName() {
        String[] parts = value.split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }

    /**
     * Retorna as iniciais do nome.
     * Exemplo: "João Silva" -> "JS"
     */
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

    /**
     * Verifica se o nome é composto (tem mais de uma palavra).
     */
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
