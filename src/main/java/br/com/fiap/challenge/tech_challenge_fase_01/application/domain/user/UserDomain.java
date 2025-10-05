package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

/**
 * Entidade de Domínio Rica - User
 * 
 * Esta classe representa o conceito de Usuário no domínio da aplicação.
 * Seguindo os princípios de Domain-Driven Design (DDD):
 * - Encapsula regras de negócio
 * - Valida seu próprio estado
 * - Fornece métodos de fábrica para criação
 * - Protege invariantes do domínio
 */
@Getter
@Builder
public class UserDomain {

    private String id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RolesEnum> role;
    private boolean isActive;

    // Construtor privado para forçar uso dos factory methods
    private UserDomain(String id, String name, String email, String login,
            String password, LocalDateTime createdAt, LocalDateTime updatedAt,
            List<RolesEnum> role, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
        this.isActive = isActive;
    }

    // Factory Methods para criação

    /**
     * Cria um novo usuário cliente.
     * Aplica as regras de negócio para criação de cliente.
     */
    public static UserDomain createClient(String name, String email, String login, String password) {
        validateRequiredFields(name, email, login, password);
        validateEmail(email);
        validateLogin(login);
        validatePassword(password);

        return UserDomain.builder()
                .name(name.trim())
                .email(email.trim().toLowerCase())
                .login(login.trim().toLowerCase())
                .password(password) // Senha já deve vir criptografada
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .role(List.of(RolesEnum.CLIENT))
                .build();
    }

    /**
     * Cria um novo usuário proprietário (owner).
     * Aplica as regras de negócio para criação de proprietário.
     */
    public static UserDomain createOwner(String name, String email, String login, String password) {
        validateRequiredFields(name, email, login, password);
        validateEmail(email);
        validateLogin(login);
        validatePassword(password);

        return UserDomain.builder()
                .name(name.trim())
                .email(email.trim().toLowerCase())
                .login(login.trim().toLowerCase())
                .password(password) // Senha já deve vir criptografada
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .role(List.of(RolesEnum.OWNER))
                .build();
    }

    // Métodos de Comportamento (Business Logic)

    /**
     * Atualiza as informações básicas do usuário.
     * Valida os novos dados antes de aplicar.
     */
    public void updateInfo(String name, String email, String login) {
        validateRequiredFieldsForUpdate(name, email, login);
        validateEmail(email);
        validateLogin(login);

        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.login = login.trim().toLowerCase();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Altera a senha do usuário.
     * A senha deve já vir criptografada.
     */
    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }

        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Desativa o usuário.
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Ativa o usuário.
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica se o usuário tem uma role específica.
     */
    public boolean hasRole(RolesEnum role) {
        return this.role != null && this.role.contains(role);
    }

    /**
     * Verifica se o usuário é um cliente.
     */
    public boolean isClient() {
        return hasRole(RolesEnum.CLIENT);
    }

    /**
     * Verifica se o usuário é um proprietário.
     */
    public boolean isOwner() {
        return hasRole(RolesEnum.OWNER);
    }

    // Métodos de Validação (Invariantes do Domínio)

    private static void validateRequiredFields(String name, String email, String login, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login é obrigatório");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
    }

    private static void validateRequiredFieldsForUpdate(String name, String email, String login) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login é obrigatório");
        }
    }

    private static void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

    private static void validateLogin(String login) {
        if (login.length() < 3) {
            throw new IllegalArgumentException("Login deve ter no mínimo 3 caracteres");
        }
        if (login.length() > 50) {
            throw new IllegalArgumentException("Login deve ter no máximo 50 caracteres");
        }
        if (!login.matches("^[a-zA-Z0-9._-]+$")) {
            throw new IllegalArgumentException(
                    "Login deve conter apenas letras, números, ponto, hífen e underscore");
        }
    }

    private static void validatePassword(String password) {
        if (password.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        }
    }

    // Métodos auxiliares para Lombok Builder

    /**
     * Classe Builder customizada para garantir validações.
     */
    public static class UserDomainBuilder {
        // Lombok gera automaticamente os métodos do builder
        // Podemos adicionar métodos customizados aqui se necessário
    }

    // Sobrescrita de equals e hashCode baseado em ID (Entity pattern)

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDomain that = (UserDomain) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserDomain{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}
