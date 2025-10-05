package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
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

    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 100;
    private static final int LOGIN_MIN_LENGTH = 3;
    private static final int LOGIN_MAX_LENGTH = 50;
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 100;
    private static final int EMAIL_MAX_LENGTH = 255;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String LOGIN_PATTERN = "^[a-zA-Z0-9._-]+$";
    private static final String NAME_PATTERN = "^[a-zA-ZÀ-ÿ\\s.'-]+$"; // Aceita letras, espaços, acentos, ponto,
                                                                       // apóstrofo e hífen
    private String id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RolesEnum> role;
    private boolean isActive;

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

    public void updateInfo(String name, String email, String login) {
        if (!this.isActive) {
            throw new BusinessRuleException("Não é possível atualizar informações de usuário inativo");
        }

        validateRequiredFieldsForUpdate(name, email, login);

        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.login = login.trim().toLowerCase();
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new InvalidFieldException("password", "Nova senha não pode ser vazia");
        }

        // Validação adicional: não permitir senha igual à atual
        if (this.password != null && this.password.equals(newPassword)) {
            throw new BusinessRuleException("A nova senha deve ser diferente da senha atual");
        }

        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (!this.isActive) {
            throw new BusinessRuleException("Usuário já está inativo");
        }

        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.isActive) {
            throw new BusinessRuleException("Usuário já está ativo");
        }

        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasRole(RolesEnum role) {
        return this.role != null && this.role.contains(role);
    }

    public boolean isClient() {
        return hasRole(RolesEnum.CLIENT);
    }

    public boolean isOwner() {
        return hasRole(RolesEnum.OWNER);
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void ensureIsActive() {
        if (!this.isActive) {
            throw new BusinessRuleException("Operação não permitida: usuário está inativo");
        }
    }

    public void ensureIsOwner() {
        if (!isOwner()) {
            throw new BusinessRuleException("Operação permitida apenas para proprietários");
        }
    }

    public void ensureHasRole(RolesEnum requiredRole) {
        if (!hasRole(requiredRole)) {
            throw new BusinessRuleException(
                    String.format("Operação requer a role: %s", requiredRole));
        }
    }

    // Métodos de Validação (Invariantes do Domínio)

    private static void validateRequiredFields(String name, String email, String login, String password) {
        validateName(name);
        validateEmail(email);
        validateLogin(login);
        validatePassword(password);
    }

    private static void validateRequiredFieldsForUpdate(String name, String email, String login) {
        validateName(name);
        validateEmail(email);
        validateLogin(login);
    }

    /**
     * Valida o nome do usuário.
     * 
     * Regras de negócio:
     * - Não pode ser nulo ou vazio
     * - Deve ter entre 2 e 100 caracteres
     * - Deve conter apenas letras, espaços, acentos e caracteres especiais
     * permitidos (. ' -)
     */
    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidFieldException("name", "Nome é obrigatório");
        }

        String trimmedName = name.trim();

        if (trimmedName.length() < NAME_MIN_LENGTH) {
            throw new InvalidFieldException("name",
                    String.format("Nome deve ter no mínimo %d caracteres", NAME_MIN_LENGTH));
        }

        if (trimmedName.length() > NAME_MAX_LENGTH) {
            throw new InvalidFieldException("name",
                    String.format("Nome deve ter no máximo %d caracteres", NAME_MAX_LENGTH));
        }

        if (!trimmedName.matches(NAME_PATTERN)) {
            throw new InvalidFieldException("name",
                    "Nome deve conter apenas letras, espaços e caracteres especiais permitidos (. ' -)");
        }
    }

    /**
     * Valida o email do usuário.
     * 
     * Regras de negócio:
     * - Não pode ser nulo ou vazio
     * - Deve ter formato válido de email
     * - Deve ter no máximo 255 caracteres
     */
    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidFieldException("email", "Email é obrigatório");
        }

        String trimmedEmail = email.trim();

        if (trimmedEmail.length() > EMAIL_MAX_LENGTH) {
            throw new InvalidFieldException("email",
                    String.format("Email deve ter no máximo %d caracteres", EMAIL_MAX_LENGTH));
        }

        if (!trimmedEmail.matches(EMAIL_PATTERN)) {
            throw new InvalidFieldException("email", "Email inválido. Use o formato: usuario@dominio.com");
        }
    }

    /**
     * Valida o login do usuário.
     * 
     * Regras de negócio:
     * - Não pode ser nulo ou vazio
     * - Deve ter entre 3 e 50 caracteres
     * - Deve conter apenas letras, números e caracteres especiais permitidos (. _
     * -)
     * - Não pode começar ou terminar com caracteres especiais
     */
    private static void validateLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new InvalidFieldException("login", "Login é obrigatório");
        }

        String trimmedLogin = login.trim();

        if (trimmedLogin.length() < LOGIN_MIN_LENGTH) {
            throw new InvalidFieldException("login",
                    String.format("Login deve ter no mínimo %d caracteres", LOGIN_MIN_LENGTH));
        }

        if (trimmedLogin.length() > LOGIN_MAX_LENGTH) {
            throw new InvalidFieldException("login",
                    String.format("Login deve ter no máximo %d caracteres", LOGIN_MAX_LENGTH));
        }

        if (!trimmedLogin.matches(LOGIN_PATTERN)) {
            throw new InvalidFieldException("login",
                    "Login deve conter apenas letras, números e caracteres especiais (. _ -)");
        }

        // Valida que não começa ou termina com caracteres especiais
        char firstChar = trimmedLogin.charAt(0);
        char lastChar = trimmedLogin.charAt(trimmedLogin.length() - 1);

        if (!Character.isLetterOrDigit(firstChar) || !Character.isLetterOrDigit(lastChar)) {
            throw new InvalidFieldException("login",
                    "Login deve começar e terminar com letra ou número");
        }
    }

    /**
     * Valida a senha do usuário.
     * 
     * Regras de negócio:
     * - Não pode ser nula ou vazia
     * - Deve ter entre 6 e 100 caracteres
     * 
     * Nota: A senha deve chegar já criptografada nos Use Cases.
     * Esta validação é para a senha em texto plano antes da criptografia.
     */
    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidFieldException("password", "Senha é obrigatória");
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new InvalidFieldException("password",
                    String.format("Senha deve ter no mínimo %d caracteres", PASSWORD_MIN_LENGTH));
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new InvalidFieldException("password",
                    String.format("Senha deve ter no máximo %d caracteres", PASSWORD_MAX_LENGTH));
        }

        // Validação de complexidade (regra de negócio)
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        if (!hasLetter) {
            throw new InvalidFieldException("password",
                    "Senha deve conter pelo menos uma letra");
        }

        if (!hasDigit) {
            throw new InvalidFieldException("password",
                    "Senha deve conter pelo menos um número");
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
